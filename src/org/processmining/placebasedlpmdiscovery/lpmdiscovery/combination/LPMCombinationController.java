package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationAndEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth.ContextLPMTreeBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth.LPMTreeBuilder;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;

import java.util.HashSet;
import java.util.Set;

public class LPMCombinationController {

    private final PlaceBasedLPMDiscoveryParameters parameters;
    private LPMFiltrationAndEvaluationController lpmFiltrationAndEvaluationController;
    private int currentNumPlaces;
    private CombinationGuard guard;
    private final RunningContext runningContext;

    public LPMCombinationController(PlaceBasedLPMDiscoveryParameters parameters, RunningContext runningContext) {
        this.parameters = parameters;
        this.runningContext = runningContext;

        this.currentNumPlaces = 1;
        this.guard = (lpm, place) -> true;

        this.lpmFiltrationAndEvaluationController = new LPMFiltrationAndEvaluationController();
        Main.getAnalyzer().getStatistics().getGeneralStatistics().setProximity(this.parameters.getLpmCombinationParameters().getLpmProximity());
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
        if (this.parameters.getEventAttributeSummary().isEmpty()) {
            LPMTreeBuilder treeBuilder = new LPMTreeBuilder(
                    log, new HashSet<>(places), this.parameters.getLpmCombinationParameters(), this.runningContext);
            Main.getInterrupterSubject().addObserver(treeBuilder);
            System.out.println("========Building tree========");
            MainFPGrowthLPMTree tree = treeBuilder.buildTree();
            Main.getInterrupterSubject().addObserver(tree);
            System.out.println("========End building tree========");

            return tree.getLPMs(lpmFiltrationAndEvaluationController, count);
        } else {
            ContextLPMTreeBuilder treeBuilder = new ContextLPMTreeBuilder(
                    log, new HashSet<>(places), this.parameters.getLpmCombinationParameters(),
                    this.parameters.getEventAttributeSummary(), runningContext);
            Main.getInterrupterSubject().addObserver(treeBuilder);
            System.out.println("========Building tree========");
            MainFPGrowthLPMTree tree = treeBuilder.buildTree();
            Main.getInterrupterSubject().addObserver(tree);
            System.out.println("========End building tree========");

            return tree.getLPMs(lpmFiltrationAndEvaluationController, count);
        }
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
