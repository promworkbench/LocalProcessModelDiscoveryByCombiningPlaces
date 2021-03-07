package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationAndEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth.FPGrowthLPMDiscoveryTreeBuilderFirstEdition;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth.FPGrowthPlaceFollowGraphBuilder;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.FPGrowthPlaceFollowGraph;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.util.HashSet;
import java.util.Set;

public class LPMCombinationController {

    private final LPMCombinationParameters parameters;
    private LPMFiltrationAndEvaluationController lpmFiltrationAndEvaluationController;
    private int currentNumPlaces;
    private CombinationGuard guard;

    public LPMCombinationController(LPMCombinationParameters parameters) {
        this.parameters = parameters;
        this.currentNumPlaces = 1;
        this.guard = (lpm, place) -> true;

        this.lpmFiltrationAndEvaluationController = new LPMFiltrationAndEvaluationController();
        Main.getAnalyzer().setProximity(parameters.getLpmProximity());
    }

    public void setCombinationGuard(CombinationGuard guard) {
        this.guard = guard;
    }

    public Set<LocalProcessModel> combine(Set<Place> places) {
        Set<LocalProcessModel> lpms = LocalProcessModelUtils.convertPlacesToLPMs(places);
        Main.getAnalyzer().addLPMsDiscovered(1, lpms.size());

        Set<LocalProcessModel> finalSet = new HashSet<>();

        lpms = this.lpmFiltrationAndEvaluationController.filterLPMs(lpms);
        Main.getAnalyzer().addLPMsFiltered(1, lpms.size());

        if (this.parameters.getMinNumPlaces() <= 1)
            finalSet.addAll(lpms);

        return combineRecursive(lpms, lpms, finalSet);
    }

    private Set<LocalProcessModel> combineRecursive(Set<LocalProcessModel> lpms,
                                                    Set<LocalProcessModel> places,
                                                    Set<LocalProcessModel> finalSet) {
        System.out.println("In combine " + this.currentNumPlaces + ": " + lpms.size());

        if (this.currentNumPlaces >= this.parameters.getMaxNumPlaces()) {
            return this.lpmFiltrationAndEvaluationController.filterFinals(finalSet);
        }

        Set<LocalProcessModel> resSet = new HashSet<>();
        this.currentNumPlaces++;

        int discoveredLPMCount = 0;
        Set<Integer> discardedHashCodes = new HashSet<>();
        // combine new local process models
        for (LocalProcessModel lpm : lpms) {
            for (LocalProcessModel place : places) {
                if (this.guard.satisfies(lpm, place)) {
                    discoveredLPMCount++;
                    LocalProcessModel intermediateLPM = new LocalProcessModel(lpm);
                    intermediateLPM.addLPM(place);
                    if (discardedHashCodes.contains(intermediateLPM.hashCode()))
                        continue;

                    if (!resSet.contains(intermediateLPM) &&
                            this.lpmFiltrationAndEvaluationController.shouldKeepLPM(intermediateLPM))
                        resSet.add(intermediateLPM);
                    else
                        discardedHashCodes.add(intermediateLPM.hashCode());
                }
            }
        }

        Main.getAnalyzer().addLPMsDiscovered(this.currentNumPlaces, discoveredLPMCount);
        Main.getAnalyzer().addLPMsFiltered(this.currentNumPlaces, resSet.size());

        if (this.currentNumPlaces >= this.parameters.getMinNumPlaces())
            finalSet.addAll(resSet);

        return this.combineRecursive(resSet, places, finalSet);
    }

    public Set<LocalProcessModel> combineUsingFPGrowth(Set<Place> places, XLog log, int localDistance, int count) {
        // Build the place graph (we can use it to order the places)
        FPGrowthPlaceFollowGraphBuilder graphBuilder = new FPGrowthPlaceFollowGraphBuilder(
                log,
                new HashSet<>(places),
                localDistance);
        FPGrowthPlaceFollowGraph graph = graphBuilder.buildGraph();
        Main.getAnalyzer().setPlacesAverageOutDegree(graph.getAverageNodeOutDegree());

//        FPGrowthLPMDiscoveryTreeBuilderSecondEdition treeBuilder = new FPGrowthLPMDiscoveryTreeBuilderSecondEdition(
//        FPGrowthLPMDiscoveryTreeBuilder treeBuilder = new FPGrowthLPMDiscoveryTreeBuilder(
        FPGrowthLPMDiscoveryTreeBuilderFirstEdition treeBuilder = new FPGrowthLPMDiscoveryTreeBuilderFirstEdition(
                log, new HashSet<>(places), this.parameters);
        Main.getInterrupterSubject().addObserver(treeBuilder);
        System.out.println("========Building tree========");
        MainFPGrowthLPMTree tree = treeBuilder.buildTree();
        Main.getInterrupterSubject().addObserver(tree);
        System.out.println("========End building tree========");

        return tree.getLPMs(lpmFiltrationAndEvaluationController, count);
    }

    private LocalProcessModel postProcessLPM(LocalProcessModel lpm) {
//        RemoveStructuralRedundantPlacesParameters parameters = new RemoveStructuralRedundantPlacesParameters();
//        parameters.setTryConnections(false);
//        parameters.setMessageLevel(0);
//        return LocalProcessModelUtils.getLPMFromAcceptingPetriNetRepresentation(
//                (new RemoveStructuralRedundantPlacesPlugin())
//                        .run(Main.getContext(), LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm), parameters));
        return lpm;
    }

    public void setFiltrationController(LPMFiltrationAndEvaluationController lpmFiltrationAndEvaluationController) {
        this.lpmFiltrationAndEvaluationController = lpmFiltrationAndEvaluationController;
    }
}
