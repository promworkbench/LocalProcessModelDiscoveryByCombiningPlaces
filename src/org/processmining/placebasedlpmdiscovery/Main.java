package org.processmining.placebasedlpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.analysis.statistics.Statistics;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationController;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex.AndCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple.NotContainingCoveringPlacesCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple.SameActivityCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterFactory;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterParameters;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilterId;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationAndEvaluationController;
import org.processmining.placebasedlpmdiscovery.model.interruptible.InterrupterSubject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.placechooser.MainPlaceChooser;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.plugins.utils.ProvidedObjectHelper;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.Analyzer;

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

    public static Analyzer getAnalyzer() {
        return Analyzer;
    }

    public static InterrupterSubject getInterrupterSubject() {
        return interrupterSubject;
    }

    public static Object[] run(XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        setUpAnalyzer(log);
        LPMResult lpmResult;
        Statistics statistics;
        PlaceSet placeSet;

        Analyzer.totalExecution.start();

        try {

            // Use some place generator to extract places from the log
            PlaceDiscoveryResult result = PlaceDiscovery.discover(log, parameters.getPlaceDiscoveryParameters());
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

        return new Object[] {lpmResult, statistics, placeSet};
    }

    public static Object[] run(Set<Place> places, XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        setUpAnalyzer(log);
        LPMResult lpmResult;
        Statistics statistics;
        PlaceSet placeSet;

        Analyzer.totalExecution.start();

        try {
            lpmResult = Main.discover(places, log, parameters);
            statistics = Analyzer.getStatistics();
            placeSet = new PlaceSet(places);
        } finally {
            Analyzer.totalExecution.stop();
            Analyzer.write();
            Analyzer = null;
        }

        return new Object[] {lpmResult, statistics, placeSet};
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

            // setup the combination controller
            LPMCombinationController controller = new LPMCombinationController(parameters);

            // set guard
            controller.setCombinationGuard(new AndCombinationGuard(
                    new SameActivityCombinationGuard(), new NotContainingCoveringPlacesCombinationGuard()));

            // setup filtration controller
            LPMFiltrationAndEvaluationController filtrationController = new LPMFiltrationAndEvaluationController();
            // set evaluator
            LPMEvaluatorFactory evaluatorFactory = new LPMEvaluatorFactory();
            filtrationController.setEvaluatorFactory(evaluatorFactory);

            // set filters
            LPMFilterParameters filterParameters = parameters.getLpmFilterParameters();
            LPMFilterFactory filterFactory = new LPMFilterFactory(filterParameters);
            for (LPMFilterId filterId : filterParameters.getLPMFilterIds()) {
                LPMFilter filter = filterFactory.getLPMFilter(filterId);
                filtrationController.addLPMFilter(filter, filter.needsEvaluation());
            }
            controller.setFiltrationController(filtrationController);
//        controller.addFinalLPMFilter(new SubLPMFilter());

//        Set<LocalProcessModel> finalLpms = controller.combine(places);

            Analyzer.logCountPlacesUsed(places.size());

            result.addAll(controller.combineUsingFPGrowth(places, log,
                    parameters.getLpmCombinationParameters().getLpmProximity(), parameters.getLpmCount()));

            Analyzer.logAllLpmDiscovered(result.size());
            if (result.size() > 0) {
                // normalize the fitting windows score
                double max = result.highestScoringElement((LocalProcessModel lpm) -> lpm.getAdditionalInfo()
                        .getEvaluationResult().getEvaluationResult(LPMEvaluationResultId.FittingWindowsEvaluationResult).getResult())
                        .getAdditionalInfo().getEvaluationResult()
                        .getEvaluationResult(LPMEvaluationResultId.FittingWindowsEvaluationResult).getResult();
                result.edit(lpm -> ((FittingWindowsEvaluationResult) lpm.getAdditionalInfo()
                        .getEvaluationResult()
                        .getEvaluationResult(LPMEvaluationResultId.FittingWindowsEvaluationResult))
                        .normalizeResult(max, 0));

                EvaluationResultAggregateOperation aggregateOperation = new EvaluationResultAggregateOperation();
                result.sort((LocalProcessModel lpm1, LocalProcessModel lpm2) -> Double.compare(
                        lpm1.getAdditionalInfo().getEvaluationResult().getResult(aggregateOperation),
                        lpm2.getAdditionalInfo().getEvaluationResult().getResult(aggregateOperation)));
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
