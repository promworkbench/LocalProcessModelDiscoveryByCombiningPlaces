package org.processmining.placebasedlpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.Analyzer;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationController;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.StandardLPMCombinationController;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple.NotContainingCoveringPlacesCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple.SameActivityCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.directors.FromParametersLPMDiscoveryDirector;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterFactory;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterParameters;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilterId;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMCollectorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMCollectorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.main.StandardLPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.placechooser.MainPlaceChooser;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PetriNetPlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.StandardPlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public class Main {

    public static LPMDiscoveryBuilder createDefaultBuilder(XLog log, PlaceSet placeSet,
                                                           PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryBuilder builder = new StandardLPMDiscoveryBuilder();

        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, builder, runningContext);

        // set place discovery
        builder.setPlaceDiscovery(placeSet);

        // set place chooser
        LEFRMatrix lefrMatrix =
                runningContext.getAnalyzer().logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity());
        builder.setPlaceChooser(new MainPlaceChooser(log, parameters.getPlaceChooserParameters(), lefrMatrix));

        // set filtration and evaluation controllers
        setupStandardEvaluationAndFiltrationControllers(parameters, builder);

        // set lpm combination controller
        LPMCombinationController controller = // TODO: Should be created in the builder - have a factory
                new StandardLPMCombinationController(log, parameters,
                        new LPMFiltrationController(new LPMEvaluationController()), new LPMEvaluationController(),
                        runningContext);
        builder.setLPMCombination(controller);

        return builder;
    }

    public static LPMDiscoveryBuilder createDefaultBuilder(XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryBuilder builder = new StandardLPMDiscoveryBuilder();

        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, builder, runningContext);

        // set place discovery
        builder.setPlaceDiscovery(new StandardPlaceDiscovery(log, parameters.getPlaceDiscoveryParameters()));

        // set place chooser
        LEFRMatrix lefrMatrix =
                runningContext.getAnalyzer().logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity());
        builder.setPlaceChooser(new MainPlaceChooser(log, parameters.getPlaceChooserParameters(), lefrMatrix));

        // set lpm combination controller
        LPMCombinationController controller =
                new StandardLPMCombinationController(log, parameters,
                        new LPMFiltrationController(new LPMEvaluationController()), new LPMEvaluationController(),
                        runningContext);
        builder.setLPMCombination(controller);

        // set filtration and evaluation controllers
        setupStandardEvaluationAndFiltrationControllers(parameters, builder);
        return builder;
    }

    public static LPMDiscoveryBuilder createDefaultForPetriNetBuilder(XLog log, Petrinet petrinet,
                                                                      PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryBuilder builder = new StandardLPMDiscoveryBuilder();

        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, builder, runningContext);

        // set place discovery
        builder.setPlaceDiscovery(new PetriNetPlaceDiscovery(petrinet));

        // set place chooser
        LEFRMatrix lefrMatrix =
                runningContext.getAnalyzer().logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity());
        builder.setPlaceChooser(new MainPlaceChooser(log, parameters.getPlaceChooserParameters(), lefrMatrix));

        // set lpm combination controller
        LPMCombinationController controller =
                new StandardLPMCombinationController(log, parameters,
                        new LPMFiltrationController(new LPMEvaluationController()), new LPMEvaluationController(),
                        runningContext);
        builder.setLPMCombination(controller);

        // set filtration and evaluation controllers
        setupStandardEvaluationAndFiltrationControllers(parameters, builder);
        return builder;
    }

    private static void setupStandardBase(XLog log, LPMDiscoveryBuilder builder, RunningContext runningContext) {
        runningContext.setAnalyzer(new Analyzer(log));
        builder.setRunningContext(runningContext);
    }

    private static void setupStandardEvaluationAndFiltrationControllers(PlaceBasedLPMDiscoveryParameters parameters,
                                                                        LPMDiscoveryBuilder builder) {
        FromParametersLPMDiscoveryDirector director = new FromParametersLPMDiscoveryDirector(builder, parameters);
        director.make();
    }
}
