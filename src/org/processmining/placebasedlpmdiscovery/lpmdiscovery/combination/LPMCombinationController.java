package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationAndEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth.LPMTreeBuilder;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;

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
        Main.getAnalyzer().getStatistics().getGeneralStatistics().setProximity(parameters.getLpmProximity());
    }

    public void setCombinationGuard(CombinationGuard guard) {
        this.guard = guard;
    }

    public Set<LocalProcessModel> combineUsingFPGrowth(Set<Place> places, XLog log, int localDistance, int count) {
        // Build the place graph (we can use it to order the places)
//        FPGrowthPlaceFollowGraphBuilder graphBuilder = new FPGrowthPlaceFollowGraphBuilder(
//                log,
//                new HashSet<>(places),
//                localDistance);
//        FPGrowthPlaceFollowGraph graph = graphBuilder.buildGraph();

//        FPGrowthLPMDiscoveryTreeBuilderSecondEdition treeBuilder = new FPGrowthLPMDiscoveryTreeBuilderSecondEdition(
//        FPGrowthLPMDiscoveryTreeBuilder treeBuilder = new FPGrowthLPMDiscoveryTreeBuilder(
        LPMTreeBuilder treeBuilder = new LPMTreeBuilder(
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
