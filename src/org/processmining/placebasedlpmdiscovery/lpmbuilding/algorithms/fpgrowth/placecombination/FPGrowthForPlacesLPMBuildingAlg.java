package org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.fpgrowth.placecombination;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LogAnalyzer;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlg;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.fpgrowth.placecombination.utils.LPMFromBranchCombinationValidityChecker;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.fpgrowth.placecombination.utils.WindowLPMTreeValidLPMsRandomTraversal;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.LPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters.FPGrowthForPlacesLPMBuildingParameters;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters.LPMBuildingParameters;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.DefaultLPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.LPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationParameters;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowInfo;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowLogTraversal;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.IntegerMappedLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.extra.AbstractActivityMapping;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers.WindowTotalCounter;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.*;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.placechooser.MainPlaceChooser;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.*;
import java.util.stream.Collectors;

public class FPGrowthForPlacesLPMBuildingAlg implements LPMBuildingAlg {

    private final LPMEvaluationController evaluationController;

    public FPGrowthForPlacesLPMBuildingAlg(LPMEvaluationController evaluationController) {
        this.evaluationController = evaluationController;
    }


    @Override
    public LPMBuildingResult build(LPMBuildingInput input, LPMBuildingParameters parameters) {
        // cast input and parameters
        if (!(parameters instanceof FPGrowthForPlacesLPMBuildingParameters)) {
            throw new IllegalArgumentException("The FPGrowthForPlacesLPMBuildingAlg does not work for " +
                    "parameters of type " + parameters.getClass());
        }
        FPGrowthForPlacesLPMBuildingParameters cParameters = (FPGrowthForPlacesLPMBuildingParameters) parameters;

        if (!(input instanceof FPGrowthForPlacesLPMBuildingInput)) {
            throw new IllegalArgumentException("The FPGrowthForPlacesLPMBuildingAlg does not work for " +
                    "input of type " + input.getClass());
        }
        FPGrowthForPlacesLPMBuildingInput cInput = (FPGrowthForPlacesLPMBuildingInput) input;

        // choose places
        LogAnalyzer logAnalyzer = new LogAnalyzer(cInput.getLog().getOriginalLog());
        LEFRMatrix lefrMatrix = logAnalyzer.getLEFRMatrix(cParameters.getLPMCombinationParameters().getLpmProximity());
        PlaceChooser placeChooser = new MainPlaceChooser(cInput.getLog().getOriginalLog(),
                cParameters.getPlaceChooserParameters(), lefrMatrix);
        Set<Place> places = placeChooser.choose(cInput.getPlaces(),
                cParameters.getPlaceChooserParameters().getPlaceLimit());

        // build lpms
        return new DefaultLPMBuildingResult(build(cInput.getLog(), places, cParameters.getLPMCombinationParameters()));
    }

    private MainFPGrowthLPMTree build(EventLog log, Set<Place> places, LPMCombinationParameters parameters) {
        // input
        IntegerMappedLog integerMappedLog = new IntegerMappedLog(log.getOriginalLog());
        prepareInput(places, integerMappedLog);

        // storage
        MainFPGrowthLPMTree mainTree = new MainFPGrowthLPMTree(getPlacePriorityMap(places),
                integerMappedLog.getMapping().getLabelMap(), parameters.getLpmProximity());

        // optimization helpers
        Map<Pair<Integer, Integer>, Set<Place>> inoutTransitionPlacesMap = PlaceUtils
                .getInoutTransitionPlaceSetMapping(places, integerMappedLog.getMapping().getLabelMap());
        Map<Pair<Integer, Integer>, Set<List<Place>>> inoutViaSilentPlaceMap = PlaceUtils
                .getInoutTransitionPlaceSetMappingViaSilent(places, integerMappedLog.getMapping().getLabelMap());

        WindowTotalCounter windowTotalCounter = new WindowTotalCounter();

        // traverse
        int maxWindowSize = parameters.getLpmProximity(); // max window size
        WindowLogTraversal traversal = new WindowLogTraversal(log, maxWindowSize); // traversal
        WindowLPMTree localTree = new WindowLPMTree(maxWindowSize); // window tree
        while (traversal.hasNext()) {
            WindowInfo windowInfo = traversal.next(); // next window

            windowTotalCounter.update(windowInfo.getIntWindow(), windowInfo.getWindowCount()); // update window counter

            if (windowInfo.getStartPos() == 0 && windowInfo.getEndPos() == 0) { // new trace variant new WindowLPMTree
                localTree = new WindowLPMTree(maxWindowSize);
            }
            if (windowInfo.getStartPos() != 0) { // remove branches for the removed event
                int refreshPos = windowInfo.getWindow().size() < maxWindowSize ?
                        windowInfo.getStartPos() - 1 : windowInfo.getEndPos();
                localTree.refreshPosition(refreshPos);
            }


            // process the window (only pairs with the last event need to be processed)
            List<Integer> window = windowInfo.getIntWindow();
            int newEvent = window.get(window.size() - 1);
            for (int i = 0; i < window.size() - 1; ++i) { // iterate through the first n-1 events in the window
                // get places and paths for addition
                Set<Place> placesForAddition = inoutTransitionPlacesMap.getOrDefault(
                        new Pair<>(window.get(i), newEvent), new HashSet<>());
                Set<List<Place>> paths = inoutViaSilentPlaceMap.getOrDefault(
                        new Pair<>(window.get(i), newEvent), new HashSet<>());

                // update local tree for the i-th and last event of the window
                localTree.add(window.get(i), windowInfo.getStartPos() + i, newEvent, windowInfo.getEndPos(),
                        placesForAddition, paths, obj -> integerMappedLog.getMapping().getLabelMap().get(obj));
            }
            // calculate fitting local process models
            localTree.tryAddNullChildren(newEvent, windowInfo.getEndPos());

            // transfer built local process models to the main tree
            LPMTemporaryWindowInfoCreator lpmTempInfoCreator = new LPMTemporaryWindowInfoCreator(windowInfo,
                    integerMappedLog, traversal.currentWindowParentSequence());
            addLocalTreeToMainTree(localTree, mainTree, parameters,
                    lpmTempInfoCreator, integerMappedLog.getMapping(), places);
        }

        mainTree.updateAllTotalCount(windowTotalCounter, integerMappedLog.getTraceCount(),
                integerMappedLog.getOriginalLog());
        return mainTree;
    }

    private void addLocalTreeToMainTree(WindowLPMTree localTree,
                                        MainFPGrowthLPMTree mainTree,
                                        LPMCombinationParameters parameters,
                                        LPMTemporaryWindowInfoCreator lpmTempInfoCreator,
                                        AbstractActivityMapping<Integer> labelMapping,
                                        Set<Place> places) {
        // create lpms from tree
        Map<LocalProcessModel, LPMTemporaryWindowInfo> lpms = new HashMap<>();
        WindowLPMTreeValidLPMsRandomTraversal treeTraversal = new WindowLPMTreeValidLPMsRandomTraversal(localTree);
        while (treeTraversal.hasNext()) {
            WindowLPMTreeNode n = treeTraversal.next();
            LocalProcessModel lpm = LocalProcessModelUtils.convertReplayableToLPM(
                    n.getLpm(), labelMapping.getReverseLabelMap(), places);
            lpms.put(lpm, lpmTempInfoCreator.createTempInfo(n));
        }

        // extend with concurrency
        LPMFromBranchCombinationValidityChecker lpmValidityChecker =
                new LPMFromBranchCombinationValidityChecker(labelMapping.getLabelMap());
        addBranchCombinations(lpms, parameters.getConcurrencyCardinality(), lpmValidityChecker, lpmTempInfoCreator);


        // give the lpm and the temp info to the main tree, so it can update itself
        for (Map.Entry<LocalProcessModel, LPMTemporaryWindowInfo> lpmEntry : lpms.entrySet()) {
            LocalProcessModel lpm = lpmEntry.getKey();
            if (lpm.getPlaces().size() >= parameters.getMinNumPlaces() &&
                    lpm.getPlaces().size() <= parameters.getMaxNumPlaces() &&
                    lpm.getTransitions().size() >= parameters.getMinNumTransitions() &&
                    lpm.getTransitions().size() <= parameters.getMaxNumTransitions()) {
                MainFPGrowthLPMTreeNode node = mainTree.getNode(lpm);
                LPMAdditionalInfo oldInfo = node == null ? new LPMAdditionalInfo() : node.getAdditionalInfo();
                LPMAdditionalInfo newInfo = evaluationController.updateAdditionalInfoForOneWindow(lpm,
                        lpmEntry.getValue(), oldInfo);
                mainTree.addOrUpdate(lpm, newInfo);
//                mainTree.addOrUpdate(lpm, windowInfo.getWindowCount(), windowInfo.getWindow(), lpmEntry.getValue(),
//                        windowInfo.getTraceVariantId());
            }
        }
    }

    private void addBranchCombinations(Map<LocalProcessModel, LPMTemporaryWindowInfo> lpmFiringSequenceMap,
                                       int concurrencyCardinality,
                                       LPMFromBranchCombinationValidityChecker lpmValidityChecker,
                                       LPMTemporaryWindowInfoCreator creator) {
        // TODO: We combine only by two LPMs, but more can be done
        if (concurrencyCardinality == 1)
            return;
        List<LocalProcessModel> lpms = new ArrayList<>(lpmFiringSequenceMap.keySet());
        for (int i = 0; i < lpms.size(); ++i) {
            addBranchCombinations(lpms.get(i), lpms, i + 1, lpmFiringSequenceMap, 2, concurrencyCardinality,
                    lpmValidityChecker, creator);
        }
    }

    private void addBranchCombinations(LocalProcessModel lpm, List<LocalProcessModel> lpms, int from,
                                       Map<LocalProcessModel, LPMTemporaryWindowInfo> lpmWithTemporaryInfo,
                                       int currIteration, int lastIteration,
                                       LPMFromBranchCombinationValidityChecker lpmValidityChecker,
                                       LPMTemporaryWindowInfoCreator creator) {
        LPMTemporaryWindowInfo lpmTemporaryWindowInfo = lpmWithTemporaryInfo.get(lpm);

        for (int i = from; i < lpms.size(); ++i) {
            // first check if considering the two temp infos it makes sense to merge the two models
            LPMTemporaryWindowInfo iLpmTemporaryWindowInfo = lpmWithTemporaryInfo.get(lpms.get(i));
            if (!lpmValidityChecker.shouldMerge(lpmTemporaryWindowInfo, iLpmTemporaryWindowInfo)) {
                continue;
            }

            // merge models and temp infos
            LocalProcessModel resLpm = LocalProcessModelUtils.join(lpms.get(i), lpm);
            LPMTemporaryWindowInfo resTempInfo = creator
                    .createTempInfo(lpmTemporaryWindowInfo, lpmWithTemporaryInfo.get(lpms.get(i)));

            if (lpmWithTemporaryInfo.containsKey(resLpm)) { // if already in the models do nothing
                continue;
            }

            // check validity of the merged model
            if (lpmValidityChecker.checkValidity(resLpm, resTempInfo)) {
                lpmWithTemporaryInfo.put(resLpm, resTempInfo);

                if (currIteration < lastIteration) { // do additional combinations if necessary
                    addBranchCombinations(resLpm, lpms, i + 1, lpmWithTemporaryInfo,
                            currIteration + 1, lastIteration, lpmValidityChecker, creator);
                }
            }

        }
    }

    private void prepareInput(Set<Place> places, IntegerMappedLog log) {
        // get all transitions
        Set<Transition> transitions = PlaceUtils.getAllTransitions(places);

        // add invisible transitions to the label map
        log.getMapping().addInvisibleTransitionsInLabelMap(transitions
                .stream()
                .map(Transition::getLabel)
                .collect(Collectors.toSet()));
    }

    private Map<Place, Integer> getPlacePriorityMap(Set<Place> places) {
        // TODO: should be replaced with some real ordering
        return PlaceUtils.mapPlacesToIndices(places);
    }

}
