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

    public static LPMDiscoveryBuilder createDefaultBuilder(XLog log, PlaceSet placeSet, PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryBuilder builder = new StandardLPMDiscoveryBuilder();

        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, parameters, builder, runningContext);

        // set place discovery
        builder.setPlaceDiscovery(placeSet);

        // set place chooser
        LEFRMatrix lefrMatrix = runningContext.getAnalyzer().logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity());
        builder.setPlaceChooser(new MainPlaceChooser(log, parameters.getPlaceChooserParameters(), lefrMatrix));

        // set lpm combination controller
        LPMCombinationController controller =
                new StandardLPMCombinationController(log, parameters, runningContext);
        builder.setLPMCombination(controller);
        // set guard
        builder.setCombinationGuard(new CombinationGuard(
                new SameActivityCombinationGuard(), new NotContainingCoveringPlacesCombinationGuard()));

        // set filtration and evaluation controllers
        setupStandardEvaluationAndFiltrationControllers(parameters, builder, runningContext);
        return builder;
    }

    public static LPMDiscoveryBuilder createDefaultBuilder(XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryBuilder builder = new StandardLPMDiscoveryBuilder();

        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, parameters, builder, runningContext);

        // set place discovery
        builder.setPlaceDiscovery(new StandardPlaceDiscovery(log, parameters.getPlaceDiscoveryParameters()));

        // set place chooser
        LEFRMatrix lefrMatrix = runningContext.getAnalyzer().logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity());
        builder.setPlaceChooser(new MainPlaceChooser(log, parameters.getPlaceChooserParameters(), lefrMatrix));

        // set lpm combination controller
        LPMCombinationController controller =
                new StandardLPMCombinationController(log, parameters, runningContext);
        builder.setLPMCombination(controller);
        // set guard
        builder.setCombinationGuard(new CombinationGuard(
                new SameActivityCombinationGuard(), new NotContainingCoveringPlacesCombinationGuard()));

        // set filtration and evaluation controllers
        setupStandardEvaluationAndFiltrationControllers(parameters, builder, runningContext);
        return builder;
    }

    public static LPMDiscoveryBuilder createDefaultForPetriNetBuilder(XLog log, Petrinet petrinet, PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryBuilder builder = new StandardLPMDiscoveryBuilder();

        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, parameters, builder, runningContext);

        // set place discovery
        builder.setPlaceDiscovery(new PetriNetPlaceDiscovery(petrinet));

        // set place chooser
        LEFRMatrix lefrMatrix = runningContext.getAnalyzer().logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity());
        builder.setPlaceChooser(new MainPlaceChooser(log, parameters.getPlaceChooserParameters(), lefrMatrix));

        // set lpm combination controller
        LPMCombinationController controller =
                new StandardLPMCombinationController(log, parameters, runningContext);
        builder.setLPMCombination(controller);
        // set guard
        builder.setCombinationGuard(new CombinationGuard(
                new SameActivityCombinationGuard(), new NotContainingCoveringPlacesCombinationGuard()));

        // set filtration and evaluation controllers
        setupStandardEvaluationAndFiltrationControllers(parameters, builder, runningContext);
        return builder;
    }

    private static void setupStandardBase(XLog log, PlaceBasedLPMDiscoveryParameters parameters, LPMDiscoveryBuilder builder, RunningContext runningContext) {
        runningContext.setAnalyzer(new Analyzer(log));
        builder.setRunningContext(runningContext);

        // set parameters
        builder.setParameters(parameters);
    }

    private static void setupStandardEvaluationAndFiltrationControllers(PlaceBasedLPMDiscoveryParameters parameters, LPMDiscoveryBuilder builder, RunningContext runningContext) {
        LPMFiltrationController filtrationController = new LPMFiltrationController(runningContext);
        builder.setFiltrationController(filtrationController);
        LPMEvaluationController evaluationController = new LPMEvaluationController(runningContext);
        builder.setEvaluationController(evaluationController);

        // add evaluators
        LPMCollectorFactory evaluatorFactory = new LPMCollectorFactory();
        evaluationController.setEvaluatorFactory(evaluatorFactory);
        builder.registerLPMWindowCollector(StandardLPMEvaluatorId.PassageCoverageEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMEvaluatorId.PassageCoverageEvaluator));
        builder.registerLPMWindowCollector(StandardLPMEvaluatorId.FittingWindowEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMEvaluatorId.FittingWindowEvaluator));
        builder.registerLPMWindowCollector(StandardLPMEvaluatorId.TransitionCoverageEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMEvaluatorId.TransitionCoverageEvaluator));
        builder.registerLPMWindowCollector(StandardLPMEvaluatorId.TraceSupportCountEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMEvaluatorId.TraceSupportCountEvaluator));
        builder.registerLPMWindowCollector(StandardLPMEvaluatorId.EventCoverageEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMEvaluatorId.EventCoverageEvaluator));
        builder.registerLPMWindowCollector(StandardLPMCollectorId.EventAttributeEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(StandardLPMCollectorId.EventAttributeEvaluator));

        // set filters
        LPMFilterParameters filterParameters = parameters.getLpmFilterParameters();
        LPMFilterFactory filterFactory = new LPMFilterFactory(filterParameters);
        for (LPMFilterId filterId : filterParameters.getLPMFilterIds()) {
            LPMFilter filter = filterFactory.getLPMFilter(filterId);
            builder.registerLPMFilter(filter, filter.needsEvaluation());
        }
        runningContext.setLpmFiltrationController(filtrationController);
        runningContext.setLpmEvaluationController(evaluationController);
    }
}
