package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.apache.commons.lang.NotImplementedException;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.LPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlg;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.fpgrowth.placecombination.FPGrowthForPlacesLPMBuildingAlg;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals.LPMBuildingResultTraversal;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals.LPMBuildingResultTraversalFactory;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.discovery.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryPluginParameters;

import java.util.HashSet;
import java.util.Set;

public class StandardLPMCombinationController implements LPMCombinationController {

    private final LPMFiltrationController filtrationController;
    private XLog log;
    private final PlaceBasedLPMDiscoveryPluginParameters parameters;
    private int currentNumPlaces;
    private final RunningContext runningContext;
    private LPMEvaluationController evaluationController;

    public StandardLPMCombinationController(XLog log,
                                            PlaceBasedLPMDiscoveryPluginParameters parameters,
                                            LPMFiltrationController filtrationController,
                                            LPMEvaluationController evaluationController,
                                            RunningContext runningContext) {
        this.log = log;

        this.parameters = parameters;
        this.runningContext = runningContext;
        this.filtrationController = filtrationController;
        this.evaluationController = evaluationController;

        this.currentNumPlaces = 1;

        this.runningContext
                .getAnalyzer()
                .getStatistics()
                .getGeneralStatistics()
                .setProximity(this.parameters.getLpmCombinationParameters().getLpmProximity());
    }

    public LPMDiscoveryResult combineUsingFPGrowth(Set<Place> places, int count) {
        // Build the place graph (we can use it to order the places)
//        FPGrowthPlaceFollowGraphBuilder graphBuilder = new FPGrowthPlaceFollowGraphBuilder(
//                log,
//                new HashSet<>(places),
//                localDistance);
//        FPGrowthPlaceFollowGraph graph = graphBuilder.buildGraph();

//        FPGrowthLPMDiscoveryTreeBuilderSecondEdition treeBuilder = new FPGrowthLPMDiscoveryTreeBuilderSecondEdition(
//        FPGrowthLPMDiscoveryTreeBuilder treeBuilder = new FPGrowthLPMDiscoveryTreeBuilder(
        if (this.parameters.getEventAttributeSummary().isEmpty()) {
            LPMBuildingAlg alg = new FPGrowthForPlacesLPMBuildingAlg(this.evaluationController);
            LPMBuildingResult tree = alg.build(new FPGrowthForPlacesLPMBuildingInput(new XLogWrapper(this.log),
                    places), parameters.getLPMBuildingParameters());
//            LPMTreeBuilder treeBuilder = new LPMTreeBuilder(
//                    log, new HashSet<>(places), this.parameters.getLpmCombinationParameters(), this.runningContext);
//            this.runningContext.getInterrupterSubject().addObserver(treeBuilder);
//            System.out.println("========Building tree========");
//            MainFPGrowthLPMTree tree = treeBuilder.buildTree();
//            this.runningContext.getInterrupterSubject().addObserver(tree);
//            System.out.println("========End building tree========");

            return new StandardLPMDiscoveryResult(getLPMs(tree));
        } else {
            throw new NotImplementedException();
//            ContextLPMTreeBuilder treeBuilder = new ContextLPMTreeBuilder(
//                    log, new HashSet<>(places), this.parameters.getLpmCombinationParameters(),
//                    this.parameters.getEventAttributeSummary(), runningContext);
//            Main.getInterrupterSubject().addObserver(treeBuilder);
//            System.out.println("========Building tree========");
//            MainFPGrowthLPMTree tree = treeBuilder.buildTree();
//            Main.getInterrupterSubject().addObserver(tree);
//            System.out.println("========End building tree========");
//
//            return tree.getLPMs(count);
        }
    }

    private Set<LocalProcessModel> getLPMs(LPMBuildingResult lpmBuildingResult) {
        Set<LocalProcessModel> lpms = new HashSet<>();

        LPMBuildingResultTraversal traversal = LPMBuildingResultTraversalFactory.createTraversal(lpmBuildingResult);
        while (traversal.hasNext()) {
            LocalProcessModel lpm = traversal.next();
            if (this.runningContext.getLpmFiltrationController().shouldKeepLPM(lpm)) {
                lpms.add(lpm);
            }
        }

        // TODO: The count of lpms we want returned should be used here since we don't want to go through all lpms.
        // TODO: How not sure yet

        return lpms;
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

    @Override
    public LPMDiscoveryResult combine(Set<Place> places, int count) {
        return combineUsingFPGrowth(places, count);
    }
}
