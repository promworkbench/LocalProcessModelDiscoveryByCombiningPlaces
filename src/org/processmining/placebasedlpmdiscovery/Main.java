package org.processmining.placebasedlpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.Analyzer;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.analysis.statistics.Statistics;
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
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.main.StandardLPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.interruptible.InterrupterSubject;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.placechooser.MainPlaceChooser;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser;
import org.processmining.placebasedlpmdiscovery.placediscovery.PetriNetPlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.placediscovery.StandardPlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.plugins.utils.ProvidedObjectHelper;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static PluginContext Context;
    private static Analyzer Analyzer;
    private static InterrupterSubject interrupterSubject;

    public static void setUp(PluginContext context) {
        Main.Context = context;
    }

    private static void setUpAnalyzer(XLog log) {
        Main.Analyzer = new Analyzer(log);
    }

    public static PluginContext getContext() {
        return Context;
    }

    public static Object[] run(XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        setUpAnalyzer(log);
        LPMResult lpmResult;
        Statistics statistics;
        PlaceSet placeSet;

        Analyzer.totalExecution.start();

        try {

            // Use some place generator to extract places from the log
            StandardPlaceDiscovery standardPlaceDiscovery = new StandardPlaceDiscovery(log, parameters.getPlaceDiscoveryParameters());
            PlaceDiscoveryResult result = standardPlaceDiscovery.getPlaces();
            Analyzer.getStatistics().getParameterStatistics().setPlaceDiscoveryIncluded(true);

            // Add the places as a provided object
            placeSet = new PlaceSet(result.getPlaces());
            Main.getContext().getProvidedObjectManager()
                    .createProvidedObject("Place Set - " + parameters.getPlaceDiscoveryAlgorithmId() + " from: "
                            + log.getAttributes().get("concept:name"), placeSet, PlaceSet.class, Main.getContext());
            ProvidedObjectHelper.setFavorite(Main.getContext(), placeSet);

            lpmResult = Main.discover(result.getPlaces(), log, parameters);
            statistics = Analyzer.getStatistics();
        } finally {
            Analyzer.totalExecution.stop();
            Analyzer.write();
            Analyzer = null;
        }

        return new Object[]{lpmResult, statistics, placeSet};
    }

    public static LPMDiscoveryBuilder createDefaultBuilder(XLog log, PlaceSet placeSet, PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryBuilder builder = new StandardLPMDiscoveryBuilder();

        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, parameters, builder, runningContext);

        // set place discovery
        builder.setPlaceDiscovery(placeSet);

        // set place chooser
        LEFRMatrix lefrMatrix = Analyzer.logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity());
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
        LEFRMatrix lefrMatrix = Analyzer.logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity());
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
        LPMEvaluatorFactory evaluatorFactory = new LPMEvaluatorFactory();
        evaluationController.setEvaluatorFactory(evaluatorFactory);
        builder.registerLPMWindowEvaluator(LPMEvaluatorId.PassageCoverageEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(LPMEvaluatorId.PassageCoverageEvaluator));
        builder.registerLPMWindowEvaluator(LPMEvaluatorId.FittingWindowEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(LPMEvaluatorId.FittingWindowEvaluator));
        builder.registerLPMWindowEvaluator(LPMEvaluatorId.TransitionCoverageEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(LPMEvaluatorId.TransitionCoverageEvaluator));
        builder.registerLPMWindowEvaluator(LPMEvaluatorId.TraceSupportCountEvaluator.name(),
                evaluatorFactory.getWindowEvaluator(LPMEvaluatorId.TraceSupportCountEvaluator));

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

    public static LPMResult discover(Set<Place> places, XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        // create task that will broadcast interrupt event when the time limit is met
        interrupterSubject = new InterrupterSubject();
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        interrupterSubject.notifyInterruption();
                        Analyzer.getStatistics().getGeneralStatistics().setInterrupted(true);
                        System.out.println("INTERRUPTED");
                    }
                }, parameters.getTimeLimit()
        );

        // setup statistics for the parameters
        Analyzer.setStatisticsForParameters(parameters);

        // places = PlaceDiscovery.filterPlaces(places);

        Analyzer.lpmDiscoveryExecution.start();
        LPMResult result = new LPMResult();
        try {
            // analyze log
            Analyzer.logAnalyzer.analyze(parameters.getLpmCombinationParameters().getLpmProximity());
            LEFRMatrix lefrMatrix = Analyzer.logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity());
//            Main.getContext().getProvidedObjectManager()
//                    .createProvidedObject("LEFR - " + parameters.getPlaceDiscoveryAlgorithmId() + " from: "
//                                    + log.getAttributes().get("concept:name"), lefrMatrix, LEFRMatrix.class,
//                            Main.getContext());
//            ProvidedObjectHelper.setFavorite(Main.getContext(), lefrMatrix);

            // choose places
            parameters.getPlaceChooserParameters()
                    .setFollowRelationsLimit(parameters.getLpmCombinationParameters().getLpmProximity());
            PlaceChooser placeChooser = new MainPlaceChooser(log, parameters.getPlaceChooserParameters(), lefrMatrix);
            places = placeChooser.choose(places, parameters.getPlaceChooserParameters().getPlaceLimit());

            // export chosen places
            PlaceSet placeSet = new PlaceSet(places);
            placeSet.writePassageUsage(Analyzer.logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity()));
//            Main.getContext().getProvidedObjectManager()
//                    .createProvidedObject("Chosen Place Set - " + parameters.getPlaceDiscoveryAlgorithmId() + " from: "
//                            + log.getAttributes().get("concept:name"), placeSet, PlaceSet.class, Main.getContext());
//            ProvidedObjectHelper.setFavorite(Main.getContext(), placeSet);

            RunningContext runningContext = new RunningContext();
            // setup the combination controller
            StandardLPMCombinationController controller =
                    new StandardLPMCombinationController(log, parameters, runningContext);

            // set guard
            controller.setCombinationGuard(new CombinationGuard(
                    new SameActivityCombinationGuard(), new NotContainingCoveringPlacesCombinationGuard()));

            // setup filtration controller
            LPMFiltrationController filtrationController = new LPMFiltrationController(runningContext);
            LPMEvaluationController evaluationController = new LPMEvaluationController(runningContext);
            // set evaluator
            LPMEvaluatorFactory evaluatorFactory = new LPMEvaluatorFactory();
            evaluationController.setEvaluatorFactory(evaluatorFactory);
            evaluationController.registerEvaluator(LPMEvaluatorId.PassageCoverageEvaluator.name(),
                    evaluatorFactory.getWindowEvaluator(LPMEvaluatorId.PassageCoverageEvaluator));
            evaluationController.registerEvaluator(LPMEvaluatorId.FittingWindowEvaluator.name(),
                    evaluatorFactory.getWindowEvaluator(LPMEvaluatorId.FittingWindowEvaluator));
            evaluationController.registerEvaluator(LPMEvaluatorId.TransitionCoverageEvaluator.name(),
                    evaluatorFactory.getWindowEvaluator(LPMEvaluatorId.TransitionCoverageEvaluator));
            evaluationController.registerEvaluator(LPMEvaluatorId.TraceSupportCountEvaluator.name(),
                    evaluatorFactory.getWindowEvaluator(LPMEvaluatorId.TraceSupportCountEvaluator));


            // set filters
            LPMFilterParameters filterParameters = parameters.getLpmFilterParameters();
            LPMFilterFactory filterFactory = new LPMFilterFactory(filterParameters);
            for (LPMFilterId filterId : filterParameters.getLPMFilterIds()) {
                LPMFilter filter = filterFactory.getLPMFilter(filterId);
                filtrationController.addLPMFilter(filter, filter.needsEvaluation());
            }
            runningContext.setLpmFiltrationController(filtrationController);
            runningContext.setLpmEvaluationController(evaluationController);
//        controller.addFinalLPMFilter(new SubLPMFilter());

//        Set<LocalProcessModel> finalLpms = controller.combine(places);

            Analyzer.logCountPlacesUsed(places.size());

            result.addAll(controller.combineUsingFPGrowth(places, parameters.getLpmCount()));

            Analyzer.logAllLpmDiscovered(result.size());
            if (result.size() > 0) {
                // normalize the fitting windows score
                double max = result.highestScoringElement((LocalProcessModel lpm) -> lpm.getAdditionalInfo()
                                .getEvaluationResult(LPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                                        FittingWindowsEvaluationResult.class).getResult())
                        .getAdditionalInfo().getEvaluationResult(
                                LPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                                FittingWindowsEvaluationResult.class).getResult();
                result.edit(lpm -> ((FittingWindowsEvaluationResult) lpm.getAdditionalInfo()
                        .getEvaluationResult(LPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                                FittingWindowsEvaluationResult.class))
                        .normalizeResult(max, 0));

                EvaluationResultAggregateOperation aggregateOperation = new EvaluationResultAggregateOperation();
                result.sort((LocalProcessModel lpm1, LocalProcessModel lpm2) -> Double.compare(
                        LocalProcessModelUtils.getGroupedEvaluationResult(lpm1).getResult(aggregateOperation),
                        LocalProcessModelUtils.getGroupedEvaluationResult(lpm2).getResult(aggregateOperation)));
                result.keep(parameters.getLpmCount());
            }
        } finally {
            Analyzer.logLpmReturned(result.size());
            Analyzer.lpmDiscoveryExecution.stop();
            Analyzer.close();
            timer.cancel();
        }

        return result;
    }
}
