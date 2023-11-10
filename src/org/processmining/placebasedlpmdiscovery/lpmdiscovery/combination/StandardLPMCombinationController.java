package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination;

import org.apache.commons.lang.NotImplementedException;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
//import org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth.ContextLPMTreeBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth.LPMTreeBuilder;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.main.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

import java.util.HashSet;
import java.util.Set;

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

            return new StandardLPMDiscoveryResult(tree);
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
