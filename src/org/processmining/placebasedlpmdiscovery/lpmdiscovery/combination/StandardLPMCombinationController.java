package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.apache.commons.lang.NotImplementedException;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.LPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals.LPMBuildingResultTraversal;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals.MainFPGrowthLPMTreeTraversal;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
//import org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth.ContextLPMTreeBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth.LPMTreeBuilder;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.discovery.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTreeNode;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

import java.util.*;

public class StandardLPMCombinationController implements LPMCombinationController {

    private XLog log;
    private final PlaceBasedLPMDiscoveryParameters parameters;
    private int currentNumPlaces;
    private final RunningContext runningContext;

    public StandardLPMCombinationController(XLog log,
                                            PlaceBasedLPMDiscoveryParameters parameters,
                                            RunningContext runningContext) {
        this.log = log;

        this.parameters = parameters;
        this.runningContext = runningContext;

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
            LPMTreeBuilder treeBuilder = new LPMTreeBuilder(
                    log, new HashSet<>(places), this.parameters.getLpmCombinationParameters(), this.runningContext);
            this.runningContext.getInterrupterSubject().addObserver(treeBuilder);
            System.out.println("========Building tree========");
            MainFPGrowthLPMTree tree = treeBuilder.buildTree();
            this.runningContext.getInterrupterSubject().addObserver(tree);
            System.out.println("========End building tree========");

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

    private Set<LocalProcessModel> getLPMs(MainFPGrowthLPMTree tree) {
        Set<LocalProcessModel> lpms = new HashSet<>();

        LPMBuildingResultTraversal traversal = new MainFPGrowthLPMTreeTraversal(tree);
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
