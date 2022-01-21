package org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth;

import org.apache.commons.math3.util.Pair;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers.WindowTotalCounter;
import org.processmining.placebasedlpmdiscovery.model.CanBeInterrupted;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LocalFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LocalFPGrowthLPMTreeNode;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FPGrowthLPMDiscoveryTreeBuilder implements CanBeInterrupted {

    private final XLog log;
    private final Set<Place> places;
    private final int maxDependencyLength;
    private final int minNumPlaces;
    private final int maxNumPlaces;
    private boolean stop;

    public FPGrowthLPMDiscoveryTreeBuilder(XLog log, Set<Place> places, int maxDependencyLength,
                                           int minNumPlaces, int maxNumPlaces) {
        this.log = log;
        this.places = places;
        this.maxDependencyLength = maxDependencyLength;
        this.minNumPlaces = minNumPlaces;
        this.maxNumPlaces = maxNumPlaces;
        this.stop = false;
    }

    public MainFPGrowthLPMTree buildTree() {
        WindowLog windowLog = new WindowLog(this.log); // create the integer mapped log
        Set<Transition> transitions = PlaceUtils.getAllTransitions(this.places); // get all transitions

        // add invisible transitions to the label map
        windowLog.addInvisibleTransitionsInLabelMap(transitions
                .stream()
                .map(Transition::getLabel)
                .collect(Collectors.toSet()));

        MainFPGrowthLPMTree mainTree = new MainFPGrowthLPMTree(getPlacePriorityMap(),
                windowLog.getLabelMap(), this.maxDependencyLength);

        // map transitions to places that have it as input
//        Set<String> transitionLabels = transitions.stream().map(Transition::getLabel).collect(Collectors.toSet());
//        Map<Integer, Set<Place>> inTransitionPlacesMap = PlaceUtils.getTransitionPlaceSetMapping(
//                transitionLabels, this.places, true, windowLog.getLabelMap(), true);
        Map<Pair<Integer, Integer>, Set<Place>> inoutTransitionPlacesMap = PlaceUtils
                .getInoutTransitionPlaceSetMapping(this.places, windowLog.getLabelMap());
        Map<Pair<Integer, Integer>, Set<List<Place>>> inoutViaSilentPlaceMap = PlaceUtils
                .getInoutTransitionPlaceSetMappingViaSilent(this.places, windowLog.getLabelMap());

        WindowTotalCounter windowTotalCounter = new WindowTotalCounter();

        // iterate through all traces
        for (Integer traceVariantId : windowLog.getTraceVariantIds()) {
            List<Integer> traceVariant = windowLog.getTraceVariant(traceVariantId);
            int traceCount = windowLog.getTraceVariantCount(traceVariant);

            // iterate through all windows in the trace
            LinkedList<Integer> window = new LinkedList<>();
            LocalFPGrowthLPMTree localTree = new LocalFPGrowthLPMTree();
            Main.getInterrupterSubject().addObserver(localTree);
            int eventPos = 0;
            for (int event : traceVariant) {
                if (stop) {
                    mainTree.updateAllTotalCount(windowTotalCounter);
                    Main.getAnalyzer().getStatistics().getFpGrowthStatistics().initializeMainTreeStatistics(mainTree);
                    return mainTree;
                }

                eventPos++;
                if (window.size() >= this.maxDependencyLength) {
                    window.removeFirst();
                    localTree.removeOldestBranches();
                }
                window.add(event);
                windowTotalCounter.update(window, traceCount);

                for (int i = 0; i < window.size() - 1; ++i) {
                    Set<Place> placesForAddition = inoutTransitionPlacesMap.getOrDefault(
                            new Pair<>(window.get(i), event), new HashSet<>());
                    Set<List<Place>> paths = inoutViaSilentPlaceMap.getOrDefault(
                            new Pair<>(window.get(i), event), new HashSet<>());

                    Main.getAnalyzer().getStatistics().getFpGrowthStatistics().placesAddedInLocalTree(
                            placesForAddition.size() + paths.size() * 2);
                    localTree.add(window.get(i), placesForAddition, paths, windowLog.getLabelMap(), i);
                }
                localTree.tryAddNullChildren(event, window.size() - 1);
                addLocalTreeToMainTree(localTree, mainTree, traceCount, window, windowLog);
            }
            while (window.size() > 1) {
                eventPos++;
                window.removeFirst();
                localTree.removeOldestBranches();
                windowTotalCounter.update(window, traceCount);
                addLocalTreeToMainTree(localTree, mainTree, traceCount, window, windowLog);
            }
            Main.getAnalyzer().getStatistics().getFpGrowthStatistics().traceVariantPassed();
        }

        mainTree.updateAllTotalCount(windowTotalCounter);
        Main.getAnalyzer().getStatistics().getFpGrowthStatistics().initializeMainTreeStatistics(mainTree);
        return mainTree;
    }

    private void addLocalTreeToMainTree(LocalFPGrowthLPMTree localTree, MainFPGrowthLPMTree mainTree,
                                        int windowCount, LinkedList<Integer> window, WindowLog windowLog) {
        // get the null children
        Map<LocalProcessModel, List<Integer>> lpms =
                getLPMsAndFiringSequences(windowLog.getReverseLabelMap(), localTree);
        Main.getAnalyzer().getStatistics().getFpGrowthStatistics().lpmsAddedInMainTree(lpms.size());

        // give the lpm and the window count to the main tree so it can update itself
        for (Map.Entry<LocalProcessModel, List<Integer>> lpmEntry : lpms.entrySet()) {
            if (stop)
                return;
            LocalProcessModel lpm = lpmEntry.getKey();
            if (lpm.getPlaces().size() >= this.minNumPlaces && lpm.getPlaces().size() <= this.maxNumPlaces)
                mainTree.addOrUpdate(lpm, windowCount, window, lpmEntry.getValue());
        }
    }

    private Map<LocalProcessModel, List<Integer>> getLPMsAndFiringSequences(Map<Integer, String> reversedLabelMap,
                                                                            LocalFPGrowthLPMTree localTree) {
        Set<LocalFPGrowthLPMTreeNode> nullNodes = localTree.getNullNodes();
        Map<LocalProcessModel, List<Integer>> lpmFiringSequenceMap = nullNodes // get the unique lpms
                .stream()
                .collect(Collectors.toMap(
                        n -> LocalProcessModelUtils
                                .convertReplayableToLPM(n.getLpm(), reversedLabelMap),
                        n -> n.getLpm().getFiringSequence(),
                        (n1, n2) -> n1)); // TODO: update how the firing sequences are added
        addBranchCombinations(lpmFiringSequenceMap);
        return lpmFiringSequenceMap;
    }

    private void addBranchCombinations(Map<LocalProcessModel, List<Integer>> lpmFiringSequenceMap) {
        // TODO: We combine only by two LPMs, but more can be done
        List<LocalProcessModel> lpms = new ArrayList<>(lpmFiringSequenceMap.keySet());
        for (int i = 0; i < lpms.size(); ++i) {
            for (int j = i + 1; j < lpms.size(); ++j) {
                if (stop)
                    return;

                List<Integer> fsi = lpmFiringSequenceMap.get(lpms.get(i));
                List<Integer> fsj = lpmFiringSequenceMap.get(lpms.get(j));
                if (!fsi.get(0).equals(fsj.get(0)) && !fsi.get(fsi.size() - 1).equals(fsj.get(fsj.size() - 1)))
                    continue;

                LocalProcessModel lpm = LocalProcessModelUtils.join(lpms.get(i), lpms.get(j));
                if (!lpmFiringSequenceMap.containsKey(lpm))
                    lpmFiringSequenceMap.put(lpm, fsi);
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
