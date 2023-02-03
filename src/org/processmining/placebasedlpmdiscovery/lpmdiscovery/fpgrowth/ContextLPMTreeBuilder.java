package org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth;

import org.apache.commons.math3.util.Pair;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationParameters;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.ContextWindowLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers.WindowTotalCounter;
import org.processmining.placebasedlpmdiscovery.model.interruptible.CanBeInterrupted;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.WindowLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.WindowLPMTreeNode;
import org.processmining.placebasedlpmdiscovery.replayer.Replayer;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.EventAttributeSummary;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ContextLPMTreeBuilder implements CanBeInterrupted {

    private final XLog log;
    private final Set<Place> places;
    LPMCombinationParameters parameters;
    private boolean stop;
    private ContextWindowLog windowLog;

    public ContextLPMTreeBuilder(XLog log, Set<Place> places, LPMCombinationParameters parameters, Map<String, EventAttributeSummary<?,?>> context) {
        this.log = log;
        this.places = places;
        this.parameters = parameters;
        this.stop = false;
        windowLog = new ContextWindowLog(this.log, context); // create the integer mapped log
    }

    public MainFPGrowthLPMTree buildTree() {
        Set<Transition> transitions = PlaceUtils.getAllTransitions(this.places); // get all transitions

        // add invisible transitions to the label map
        windowLog.getMapping().addInvisibleTransitionsInLabelMap(transitions
                .stream()
                .map(Transition::getLabel)
                .collect(Collectors.toSet()));

        MainFPGrowthLPMTree mainTree = new MainFPGrowthLPMTree(getPlacePriorityMap(),
                windowLog.getMapping().getLabelMap(), this.parameters.getLpmProximity());

        // map transitions to places that have it as input
//        Set<String> transitionLabels = transitions.stream().map(Transition::getLabel).collect(Collectors.toSet());
//        Map<Integer, Set<Place>> inTransitionPlacesMap = PlaceUtils.getTransitionPlaceSetMapping(
//                transitionLabels, this.places, true, windowLog.getLabelMap(), true);
        Map<Pair<Integer, Integer>, Set<Place>> inoutTransitionPlacesMap = PlaceUtils
                .getInoutTransitionPlaceSetMapping(this.places, windowLog.getMapping().getLabelMap());
        Map<Pair<Integer, Integer>, Set<List<Place>>> inoutViaSilentPlaceMap = PlaceUtils
                .getInoutTransitionPlaceSetMappingViaSilent(this.places, windowLog.getMapping().getLabelMap());

        WindowTotalCounter windowTotalCounter = new WindowTotalCounter();

        // iterate through all traces
        for (Integer traceVariantId : windowLog.getTraceVariantIds()) {
            List<Integer> traceVariantContext = windowLog.getTraceVariantContext(traceVariantId);
            List<Integer> traceVariant = windowLog.getTraceVariant(traceVariantId);
            int traceCount = windowLog.getTraceVariantCount(traceVariantContext);

            // iterate through all windows in the trace
            LinkedList<Integer> window = new LinkedList<>();
//            LocalFPGrowthLPMTree localTree = new LocalFPGrowthLPMTree();
            WindowLPMTree localTree = new WindowLPMTree(this.parameters.getLpmProximity());
            Main.getInterrupterSubject().addObserver(localTree);
            int eventPos = 0;
            for (int event : traceVariant) {
                if (stop) {
                    mainTree.updateAllTotalCount(windowTotalCounter, windowLog.getTraceCount());
                    Main.getAnalyzer().getStatistics().getFpGrowthStatistics().initializeMainTreeStatistics(mainTree);
                    return mainTree;
                }

                eventPos++;
                if (!windowLog.isUsable(traceVariantId, eventPos - 1))
                    continue;

                Main.getAnalyzer().startWindow();

                if (window.size() >= this.parameters.getLpmProximity()) {
                    window.removeFirst();
                    localTree.refreshPosition(eventPos);
                }
                window.add(event);
                windowTotalCounter.update(window, traceCount);

                for (int i = 0; i < window.size() - 1; ++i) {
                    if (!windowLog.isUsable(traceVariantId, eventPos - window.size() + 1 + i))
                        continue;

                    Set<Place> placesForAddition = inoutTransitionPlacesMap.getOrDefault(
                            new Pair<>(window.get(i), event), new HashSet<>());
                    Set<List<Place>> paths = inoutViaSilentPlaceMap.getOrDefault(
                            new Pair<>(window.get(i), event), new HashSet<>());

                    Main.getAnalyzer().getStatistics().getFpGrowthStatistics().placesAddedInLocalTree(
                            placesForAddition.size() + paths.size());
//                    localTree.add(window.get(i), placesForAddition, paths, windowLog.getLabelMap(), i);
                    localTree.add(window.get(i), eventPos - window.size() + 1 + i,
                            event, eventPos,
                            placesForAddition, paths, windowLog.getMapping().getLabelMap());
                }
                localTree.tryAddNullChildren(event, window.size() - 1);
                Main.getAnalyzer().stopWindow();
                addLocalTreeToMainTree(localTree, mainTree, traceCount, window, windowLog, traceVariantId);
            }
            if (window.size() < this.parameters.getLpmProximity()) {
                for (int i = 0; i < this.parameters.getLpmProximity() - window.size(); ++i) {
                    eventPos++;
                    localTree.refreshPosition(eventPos);
                    windowTotalCounter.update(window, traceCount);
                    addLocalTreeToMainTree(localTree, mainTree, traceCount, window, windowLog, traceVariantId);
                }
            }
            while (window.size() > 1) {
                eventPos++;
                window.removeFirst();
//                localTree.tryAddNullChildren(event, window.size() - 1);
                localTree.refreshPosition(eventPos);
                windowTotalCounter.update(window, traceCount);
                addLocalTreeToMainTree(localTree, mainTree, traceCount, window, windowLog, traceVariantId);
            }
            Main.getAnalyzer().getStatistics().getFpGrowthStatistics().traceVariantPassed();
        }

        mainTree.updateAllTotalCount(windowTotalCounter, windowLog.getTraceCount());
        Main.getAnalyzer().getStatistics().getFpGrowthStatistics().initializeMainTreeStatistics(mainTree);
        return mainTree;
    }

    private void addLocalTreeToMainTree(WindowLPMTree localTree, MainFPGrowthLPMTree mainTree,
                                        int windowCount, LinkedList<Integer> window, ContextWindowLog windowLog, Integer traceVariantId) {
        // get the null children
        Map<LocalProcessModel, List<Integer>> lpms =
                getLPMsAndFiringSequences(windowLog.getMapping().getReverseLabelMap(), localTree, window);

        // give the lpm and the window count to the main tree so it can update itself
        int countAdded = 0;
        for (Map.Entry<LocalProcessModel, List<Integer>> lpmEntry : lpms.entrySet()) {
            if (stop) {
                Main.getAnalyzer().getStatistics().getFpGrowthStatistics().lpmsAddedInMainTree(lpms.size());
                return;
            }
            LocalProcessModel lpm = lpmEntry.getKey();
            if (lpm.getPlaces().size() >= this.parameters.getMinNumPlaces() &&
                    lpm.getPlaces().size() <= this.parameters.getMaxNumPlaces() &&
                    lpm.getTransitions().size() >= this.parameters.getMinNumTransitions() &&
                    lpm.getTransitions().size() <= this.parameters.getMaxNumTransitions()) {
                mainTree.addOrUpdate(lpm, windowCount, window, lpmEntry.getValue(), traceVariantId);
                countAdded++;
            }
        }
        Main.getAnalyzer().getStatistics().getFpGrowthStatistics().lpmsAddedInMainTree(lpms.size());
    }

    private Map<LocalProcessModel, List<Integer>> getLPMsAndFiringSequences(Map<Integer, String> reversedLabelMap,
                                                                            WindowLPMTree localTree,
                                                                            List<Integer> window) {
        Set<WindowLPMTreeNode> nullNodes = localTree.getNullNodes();
        Map<LocalProcessModel, List<Integer>> lpmFiringSequenceMap = nullNodes // get the unique lpms
                .stream()
                .collect(Collectors.toMap(
                        n -> LocalProcessModelUtils
                                .convertReplayableToLPM(n.getLpm(), reversedLabelMap),
                        n -> n.getLpm().getFiringSequence(),
                        (n1, n2) -> n1)); // TODO: update how the firing sequences are added
        addBranchCombinations(lpmFiringSequenceMap, new ArrayList<>(window));
        return lpmFiringSequenceMap;
    }

    private void addBranchCombinations(Map<LocalProcessModel, List<Integer>> lpmFiringSequenceMap, List<Integer> window) {
        // TODO: We combine only by two LPMs, but more can be done
        if (parameters.getConcurrencyCardinality() == 1)
            return;
        List<LocalProcessModel> lpms = new ArrayList<>(lpmFiringSequenceMap.keySet());
        for (int i = 0; i < lpms.size(); ++i) {
            if (stop)
                return;
            addBranchCombinations(lpms.get(i), lpms, i + 1, lpmFiringSequenceMap, window, 2);
        }
    }

    private void addBranchCombinations(LocalProcessModel lpm, List<LocalProcessModel> lpms, int from,
                                       Map<LocalProcessModel, List<Integer>> lpmFiringSequenceMap,
                                       List<Integer> window, int currIteration) {
        List<Integer> fs = lpmFiringSequenceMap.get(lpm);
        for (int i = from; i < lpms.size(); ++i) {
            if (stop)
                return;

            List<Integer> fsi = lpmFiringSequenceMap.get(lpms.get(i));
            if (fsi.stream().noneMatch(fs::contains)) {
                continue;
            }
            LocalProcessModel resLpm = LocalProcessModelUtils.join(lpms.get(i), lpm);
            if (!lpmFiringSequenceMap.containsKey(resLpm)) {
                List<Integer> sequence = joinFiringSequences(fsi, fs, window);
                Replayer replayer = new Replayer(resLpm, windowLog.getMapping().getLabelMap());
                if (replayer.canReplay(sequence)) {
                    lpmFiringSequenceMap.put(resLpm, sequence);
                    if (currIteration < this.parameters.getConcurrencyCardinality()) {
                        addBranchCombinations(resLpm, lpms, i+1, lpmFiringSequenceMap, window, currIteration + 1);
                    }
                }
            }
        }
    }

    private boolean isSublist(List<Integer> list, List<Integer> sublist) {
        int i = 0;
        int j = 0;

        while (i < list.size() && j < sublist.size()) {
            if (list.get(i).equals(sublist.get(j))) {
                i++;
                j++;
            } else {
                i++;
            }
        }
        return j == sublist.size();
    }

    private List<Integer> joinFiringSequences(List<Integer> one, List<Integer> two, List<Integer> superSequence) {
        int i = 0;
        int j = 0;
        int k = 0;
        List<Integer> result = new ArrayList<>();
        while (true) {
            while (i < one.size() && windowLog.getMapping().getInvisible().contains(one.get(i))) {
                result.add(one.get(i));
                i++;
            }
            while (j < two.size() && windowLog.getMapping().getInvisible().contains(two.get(j))) {
                result.add(two.get(j));
                j++;
            }
            if (i >= one.size() && j >= two.size())
                return result;
            if (i >= one.size()) {
                result.addAll(two.subList(j, two.size()));
                return result;
            }
            if (j >= two.size()) {
                result.addAll(one.subList(i, one.size()));
                return result;
            }
            if (k >= superSequence.size()) {
                return result;
            }
            if (one.get(i).equals(two.get(j)) && one.get(i).equals(superSequence.get(k))) {
                result.add(one.get(i));
                i++;
                j++;
                k++;
            } else if (one.get(i).equals(superSequence.get(k)) && !two.get(j).equals(superSequence.get(k))) {
                result.add(one.get(i));
                i++;
                k++;
            } else if (!one.get(i).equals(superSequence.get(k)) && two.get(j).equals(superSequence.get(k))) {
                result.add(two.get(j));
                j++;
                k++;
            } else {
                k++;
            }
        }
    }

    private Map<Place, Integer> getPlacePriorityMap() {
        // TODO: should be replaced with some real ordering
        AtomicInteger counter = new AtomicInteger(1);
        return this.places
                .stream()
                .collect(Collectors.toMap(p -> p, p -> counter.getAndIncrement()));
    }


    @Override
    public void interrupt() {
        this.stop = true;
    }
}
