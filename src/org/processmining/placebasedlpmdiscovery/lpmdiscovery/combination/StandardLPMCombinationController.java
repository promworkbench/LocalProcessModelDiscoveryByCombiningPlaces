package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.apache.commons.lang.NotImplementedException;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.RunningContext;
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
    private CombinationGuard guard;
    private final RunningContext runningContext;

    public StandardLPMCombinationController(XLog log,
                                            PlaceBasedLPMDiscoveryParameters parameters,
                                            RunningContext runningContext) {
        this.log = log;

        this.parameters = parameters;
        this.runningContext = runningContext;

        this.currentNumPlaces = 1;
        this.guard = (lpm, place) -> true;

        this.runningContext
                .getAnalyzer()
                .getStatistics()
                .getGeneralStatistics()
                .setProximity(this.parameters.getLpmCombinationParameters().getLpmProximity());
    }

    public void setCombinationGuard(CombinationGuard guard) {
        this.guard = guard;
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

            return new StandardLPMDiscoveryResult(getLPMs(tree, parameters.getLpmCount()));
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

    private static void transferAdditionalInfo(MainFPGrowthLPMTreeNode node, LocalProcessModel lpm) {
        for (Map.Entry<String, LPMCollectorResult> entry : node.getAdditionalInfo().getCollectorResults().entrySet()) {
            lpm.getAdditionalInfo().addCollectorResult(entry.getKey(), entry.getValue());
        }
    }

    private Set<LocalProcessModel> getLPMs(MainFPGrowthLPMTree tree, int count) {
        Set<LocalProcessModel> lpms = new HashSet<>();
        Set<MainFPGrowthLPMTreeNode> visited = new HashSet<>();

        Queue<MainFPGrowthLPMTreeNode> queue = new LinkedList<>();
        queue.add(tree.getRoot());
        int counter = 0;
        int counter2 = 0;
        while (!queue.isEmpty()) {
            if (lpms.size() >= count) {
                return lpms;
            }
            MainFPGrowthLPMTreeNode node = queue.poll();
            if (node != tree.getRoot() && !node.getAdditionalInfo().getCollectorResults().isEmpty()) {
                LocalProcessModel lpm = node.getLPM();
                transferAdditionalInfo(node, lpm);
                if (this.runningContext.getLpmFiltrationController().shouldKeepLPM(lpm))
                    lpms.add(lpm);
            }
            queue.addAll(node.getChildren());
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
    public void setGuard(CombinationGuard guard) {
        setCombinationGuard(guard); // TODO: It should allow for more probably
    }

    @Override
    public LPMDiscoveryResult combine(Set<Place> places, int count) {
        return combineUsingFPGrowth(places, count);
    }
}
