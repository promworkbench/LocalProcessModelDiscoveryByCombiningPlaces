package org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth;

import com.google.common.collect.Lists;
import org.apache.commons.math3.util.Pair;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers.WindowTotalCounter;
import org.processmining.placebasedlpmdiscovery.model.CanBeInterrupted;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.CircularWindowTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.TraceLPMData;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.TraceLPMFactory;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FPGrowthLPMDiscoveryTreeBuilderSecondEdition implements CanBeInterrupted {

    private final XLog log;
    private final Set<Place> places;
    private final int maxDependencyLength;
    private final int minNumPlaces;
    private final int maxNumPlaces;
    private boolean stop;

    public FPGrowthLPMDiscoveryTreeBuilderSecondEdition(XLog log, Set<Place> places, int maxDependencyLength,
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

        Map<Pair<Integer, Integer>, Set<Place>> inoutTransitionPlacesMap = PlaceUtils
                .getInoutTransitionPlaceSetMapping(this.places, windowLog.getLabelMap());
        Map<Pair<Integer, Integer>, Set<List<Place>>> inoutViaSilentPlaceMap = PlaceUtils
                .getInoutTransitionPlaceSetMappingViaSilent(this.places, windowLog.getLabelMap());

        WindowTotalCounter windowTotalCounter = new WindowTotalCounter();

        // iterate through all traces
        for (Integer traceVariantId : windowLog.getTraceVariantIds()) {
            List<Integer> traceVariant = windowLog.getTraceVariant(traceVariantId);
            int traceCount = windowLog.getTraceVariantCount(traceVariant);

            TraceLPMFactory factory = new TraceLPMFactory(traceVariant, windowLog.getLabelMap());

            // iterate through all windows in the trace
            LinkedList<Integer> window = new LinkedList<>();
            CircularWindowTree<List<Place>, TraceLPMData> localTree =
                    new CircularWindowTree<>(maxDependencyLength, factory);

            int eventPos = -1;
            int windowPos = 0;
            for (int event : traceVariant) {
                if (stop) {
                    mainTree.updateAllTotalCount(windowTotalCounter);
                    Main.getAnalyzer().getStatistics().getFpGrowthStatistics().initializeMainTreeStatistics(mainTree);
                    return mainTree;
                }

                eventPos++;
                if (window.size() >= this.maxDependencyLength) {
                    window.removeFirst();
                    localTree.clear(windowPos);
                    windowPos++;
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

                    for (Place place : placesForAddition)
                        localTree.tryAddChildren(windowPos + i, eventPos, Lists.newArrayList(place));
                    for (List<Place> path : paths)
                        localTree.tryAddChildren(windowPos + i, eventPos, path);
                }
//                localTree.tryAddNullChildren(event, window.size() - 1);
                addLocalTreeToMainTree(localTree, mainTree, traceCount, window, windowLog);
            }
            while (window.size() > 1) {
                eventPos++;
                window.removeFirst();
                localTree.clear(windowPos);
                windowPos++;
                windowTotalCounter.update(window, traceCount);
                addLocalTreeToMainTree(localTree, mainTree, traceCount, window, windowLog);
            }
            Main.getAnalyzer().getStatistics().getFpGrowthStatistics().traceVariantPassed();
        }

        mainTree.updateAllTotalCount(windowTotalCounter);
        Main.getAnalyzer().getStatistics().getFpGrowthStatistics().initializeMainTreeStatistics(mainTree);
        return mainTree;
    }

    private void addLocalTreeToMainTree(CircularWindowTree<List<Place>, TraceLPMData> localTree,
                                        MainFPGrowthLPMTree mainTree,
                                        int windowCount, LinkedList<Integer> window, WindowLog windowLog) {
        // get the null children
        List<Pair<LocalProcessModel, List<Integer>>> lpms =
                getLPMsAndFiringSequences(windowLog.getReverseLabelMap(), localTree);
        Main.getAnalyzer().getStatistics().getFpGrowthStatistics().lpmsAddedInMainTree(lpms.size());

        // give the lpm and the window count to the main tree so it can update itself
        for (Pair<LocalProcessModel, List<Integer>> lpmPair : lpms) {
            if (stop)
                return;
            LocalProcessModel lpm = lpmPair.getKey();
            if (lpm.getPlaces().size() >= this.minNumPlaces && lpm.getPlaces().size() <= this.maxNumPlaces)
                mainTree.addOrUpdate(lpm, windowCount, window, lpmPair.getValue());
        }
    }

    private List<Pair<LocalProcessModel, List<Integer>>> getLPMsAndFiringSequences(Map<Integer, String> reversedLabelMap,
                                                                                   CircularWindowTree<List<Place>, TraceLPMData> localTree) {
//        Set<WindowLPMTreeNode> nullNodes = localTree.getNullNodes();
        Collection<ReplayableLocalProcessModel> rlpms = localTree.getData()
                .stream()
                .filter(data -> data.getEmptyRlpm().isPresent())
                .map(data -> data.getEmptyRlpm().get())
                .collect(Collectors.toList());

//        List<Pair<LocalProcessModel, List<Integer>>> res = rlpms
//                .stream()
//                .map(n -> new Pair<LocalProcessModel, List<Integer>>(
//                        LocalProcessModelUtils.convertReplayableToLPM(n, reversedLabelMap),
//                        n.getFiringSequence()))
//                .collect(Collectors.toList());


        Map<LocalProcessModel, List<Integer>> lpmFiringSequenceMap = rlpms // get the unique lpms
                .stream()
                .collect(Collectors.toMap(
                        n -> LocalProcessModelUtils
                                .convertReplayableToLPM(n, reversedLabelMap),
                        ReplayableLocalProcessModel::getFiringSequence,
                        (n1, n2) -> n1)); // TODO: update how the firing sequences are added
        addBranchCombinations(lpmFiringSequenceMap);

        return lpmFiringSequenceMap.entrySet().stream()
                .map(entry -> new Pair<LocalProcessModel, List<Integer>>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
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
