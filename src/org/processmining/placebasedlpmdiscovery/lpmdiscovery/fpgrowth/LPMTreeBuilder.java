package org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth;

import org.apache.commons.math3.util.Pair;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers.WindowTotalCounter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationParameters;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.WindowLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.WindowLPMTreeNode;
import org.processmining.placebasedlpmdiscovery.model.interruptible.Interruptible;
import org.processmining.placebasedlpmdiscovery.replayer.Replayer;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import org.processmining.placebasedlpmdiscovery.utils.SequenceUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class LPMTreeBuilder extends Interruptible {

    private final Set<Place> places;
    LPMCombinationParameters parameters;
    private final WindowLog windowLog;

    private final RunningContext runningContext;

    public LPMTreeBuilder(XLog log,
                          Set<Place> places,
                          LPMCombinationParameters parameters,
                          RunningContext runningContext) {
        this.places = places;
        this.parameters = parameters;
//        this.stop = false;
        windowLog = new WindowLog(log); // create the integer mapped log
        this.runningContext = runningContext;
    }

    public MainFPGrowthLPMTree buildTree() {
        Set<Transition> transitions = PlaceUtils.getAllTransitions(this.places); // get all transitions

        // add invisible transitions to the label map
        windowLog.getMapping().addInvisibleTransitionsInLabelMap(transitions
                .stream()
                .map(Transition::getLabel)
                .collect(Collectors.toSet()));

        MainFPGrowthLPMTree mainTree = new MainFPGrowthLPMTree(getPlacePriorityMap(),
                windowLog.getMapping().getLabelMap(), this.parameters.getLpmProximity(), this.runningContext);

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
            List<Integer> traceVariant = windowLog.getTraceVariant(traceVariantId); // mapped trace variant
            int traceCount = windowLog.getTraceVariantCount(traceVariant); // count of traces represented by the trace variant

            // Storage for the window
            LinkedList<Integer> window = new LinkedList<>();
            // Storage for the window tree
            WindowLPMTree localTree = new WindowLPMTree(this.parameters.getLpmProximity());
            Main.getInterrupterSubject().addObserver(localTree);
            int eventPos = 0; // position of end event of the current window
            for (int event : traceVariant) { // for each event in the trace variant
                if (stop) { // time stop
                    mainTree.updateAllTotalCount(windowTotalCounter, windowLog.getTraceCount());
                    Main.getAnalyzer().getStatistics().getFpGrowthStatistics().initializeMainTreeStatistics(mainTree);
                    return mainTree;
                }

                Main.getAnalyzer().startWindow(); // analysis

                eventPos++; // move the window (last event is the pointer)
                if (window.size() >= this.parameters.getLpmProximity()) { // if no space in the window
                    window.removeFirst(); // remove the first event
                    // remove branches for the removed event (last event position is used because modulo is used)
                    localTree.refreshPosition(eventPos);
                }
                window.add(event); // add the current event in the end
                windowTotalCounter.update(window, traceCount); // update window counter

                // process the window (only pairs with the last event need to be processed)
                for (int i = 0; i < window.size() - 1; ++i) { // iterate through the first n-1 events in the window
                    // update local tree for the i-th and last event (eventPos) of the window
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
                // calculate fitting local process models
                localTree.tryAddNullChildren(event, window.size() - 1, eventPos);
                Main.getAnalyzer().stopWindow(); // analysis
                // transfer built local process models to the main tree
                addLocalTreeToMainTree(localTree, mainTree, traceCount, window, windowLog, traceVariantId, eventPos);
            }
            // if trace is smaller than window size the next block would not change the local process models
            // because the refresh would refresh empty positions
            if (window.size() < this.parameters.getLpmProximity()) {
                for (int i = 0; i < this.parameters.getLpmProximity() - window.size(); ++i) {
                    eventPos++;
                    localTree.refreshPosition(eventPos);
                    // don't add 5 times the same window just because the trace is smaller than the window size
//                    windowTotalCounter.update(window, traceCount);
//                    addLocalTreeToMainTree(localTree, mainTree, traceCount, window, windowLog, traceVariantId);
                }
            }
            // cover smaller right end windows
            while (window.size() > 1) {
                eventPos++;
                window.removeFirst();
                localTree.refreshPosition(eventPos);
                windowTotalCounter.update(window, traceCount);
                addLocalTreeToMainTree(localTree, mainTree, traceCount, window, windowLog, traceVariantId, eventPos);
            }
            Main.getAnalyzer().getStatistics().getFpGrowthStatistics().traceVariantPassed();
        }

        mainTree.updateAllTotalCount(windowTotalCounter, windowLog.getTraceCount());
        Main.getAnalyzer().getStatistics().getFpGrowthStatistics().initializeMainTreeStatistics(mainTree);
        return mainTree;
    }

    private void addLocalTreeToMainTree(WindowLPMTree localTree,
                                        MainFPGrowthLPMTree mainTree,
                                        int windowCount,
                                        LinkedList<Integer> window,
                                        WindowLog windowLog,
                                        Integer traceVariantId,
                                        int eventPos) {
        // get the null children
        Map<LocalProcessModel, LPMTemporaryWindowInfo> lpms =
                getLPMsWithTemporaryInfo(localTree, window, windowCount, traceVariantId, eventPos);

        // give the lpm and the window count to the main tree, so it can update itself
        for (Map.Entry<LocalProcessModel, LPMTemporaryWindowInfo> lpmEntry : lpms.entrySet()) {
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
            }
        }
        Main.getAnalyzer().getStatistics().getFpGrowthStatistics().lpmsAddedInMainTree(lpms.size());
    }

    private Map<LocalProcessModel, LPMTemporaryWindowInfo> getLPMsWithTemporaryInfo(WindowLPMTree localTree,
                                                                                    List<Integer> window,
                                                                                    int windowCount,
                                                                                    Integer traceVariantId,
                                                                                    int eventPos) {
        Set<WindowLPMTreeNode> nullNodes = localTree.getNullNodes();
        Map<LocalProcessModel, LPMTemporaryWindowInfo> lpmWithTemporaryInfo = nullNodes // get the unique lpms
                .stream()
                .collect(Collectors.toMap(
                        n -> LocalProcessModelUtils
                                .convertReplayableToLPM(n.getLpm(), this.windowLog.getMapping().getReverseLabelMap(),
                                        this.places),
                        n -> new LPMTemporaryWindowInfo(
                                n.getLpm().getFiringSequence(),
                                n.getReplayedEventsIndices(),
                                window,
                                n.getLpm().getUsedPassages(),
                                this.windowLog.getMapping().getReverseLabelMap(),
                                windowCount,
                                traceVariantId,
                                eventPos,
                                this.windowLog.getOriginalTraces(traceVariantId)),
                        (n1, n2) -> n1)); // TODO: update how the firing sequences are added
        addBranchCombinations(lpmWithTemporaryInfo, new ArrayList<>(window));
        return lpmWithTemporaryInfo;
    }

    private void addBranchCombinations(Map<LocalProcessModel, LPMTemporaryWindowInfo> lpmFiringSequenceMap, List<Integer> window) {
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
                                       Map<LocalProcessModel, LPMTemporaryWindowInfo> lpmWithTemporaryInfo,
                                       List<Integer> window, int currIteration) {
        LPMTemporaryWindowInfo lpmTemporaryWindowInfo = lpmWithTemporaryInfo.get(lpm);
        List<Integer> fs = lpmTemporaryWindowInfo.getIntegerFiringSequence();
        Collection<Integer> reIndices = lpmTemporaryWindowInfo.getReplayedEventsIndices();
        for (int i = from; i < lpms.size(); ++i) {
            if (stop)
                return;

            LPMTemporaryWindowInfo iLpmTemporaryWindowInfo = lpmWithTemporaryInfo.get(lpms.get(i));
            List<Integer> fsi = iLpmTemporaryWindowInfo.getIntegerFiringSequence();
            Collection<Integer> ireIndices = iLpmTemporaryWindowInfo.getReplayedEventsIndices();
            if (fsi.stream().noneMatch(fs::contains) || new HashSet<>(fs).containsAll(fsi) || new HashSet<>(fsi).containsAll(fs)) {
                continue;
            }
            LocalProcessModel resLpm = LocalProcessModelUtils.join(lpms.get(i), lpm);
            if (!lpmWithTemporaryInfo.containsKey(resLpm)) {
                List<Integer> sequence = SequenceUtils.joinSubsequences(fsi, fs, window, true);
                Collection<Integer> replayedEventIndices = new HashSet<>();
                replayedEventIndices.addAll(reIndices);
                replayedEventIndices.addAll(ireIndices);
                Replayer replayer = new Replayer(resLpm, windowLog.getMapping().getLabelMap());
                if (replayer.canReplay(sequence)) {
                    Set<Pair<Integer, Integer>> usedPassages = new HashSet<>();
                    usedPassages.addAll(lpmTemporaryWindowInfo.getIntegerUsedPassages());
                    usedPassages.addAll(lpmWithTemporaryInfo.get(lpms.get(i)).getIntegerUsedPassages());
                    lpmWithTemporaryInfo.put(
                            resLpm,
                            new LPMTemporaryWindowInfo(
                                    sequence,
                                    replayedEventIndices,
                                    window,
                                    usedPassages,
                                    this.windowLog.getMapping().getReverseLabelMap(),
                                    lpmTemporaryWindowInfo.getWindowCount(),
                                    lpmTemporaryWindowInfo.getTraceVariantId(),
                                    lpmTemporaryWindowInfo.getWindowLastEventPos(),
                                    lpmTemporaryWindowInfo.getOriginalTraces()));
                    if (currIteration < this.parameters.getConcurrencyCardinality()) {
                        addBranchCombinations(resLpm, lpms, i+1, lpmWithTemporaryInfo, window, currIteration + 1);
                    }
                }
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
}
