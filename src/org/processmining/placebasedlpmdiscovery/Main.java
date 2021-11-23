package org.processmining.placebasedlpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorFactory;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.loganalyzer.LogAnalyzer;
import org.processmining.placebasedlpmdiscovery.loganalyzer.LogAnalyzerParameters;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationController;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex.AndCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple.NotContainingCoveringPlacesCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple.SameActivityCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterFactory;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.LPMFilterParameters;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilterId;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationAndEvaluationController;
import org.processmining.placebasedlpmdiscovery.model.InterrupterSubject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.utils.ProjectProperties;
import org.processmining.plugins.utils.ProvidedObjectHelper;
import org.processmining.placebasedlpmdiscovery.utils.analysis.Analyzer;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class Main {

    private static PluginContext Context;
    private static Analyzer Analyzer;
    private static InterrupterSubject interrupterSubject;

    public static void setUp(PluginContext context) {
        Main.Context = context;
        ProjectProperties.initialize();
    }

    private static void setUpAnalyzer(XLog log, boolean placeDiscoveryIncluded) {
        Main.Analyzer = new Analyzer(log, placeDiscoveryIncluded);
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

    public static LPMResult run(XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        setUpAnalyzer(log, true);
        LPMResult lpmResult;

        Analyzer.totalExecution.start();

        try {

            // Use some place generator to extract places from the log
            Analyzer.setPlaceDiscoveryAlgorithmId(parameters.getPlaceDiscoveryAlgorithmId());
            PlaceDiscoveryResult result = PlaceDiscovery.discover(log, parameters.getPlaceDiscoveryParameters());

            // Add the places as a provided object
            PlaceSet placeSet = new PlaceSet(result.getPlaces());
            Main.getContext().getProvidedObjectManager()
                    .createProvidedObject("Place Set - " + parameters.getPlaceDiscoveryAlgorithmId() + " from: "
                            + log.getAttributes().get("concept:name"), placeSet, PlaceSet.class, Main.getContext());
            ProvidedObjectHelper.setFavorite(Main.getContext(), placeSet);

            lpmResult = Main.discover(result.getPlaces(), result.getLog(), parameters);
        } finally {
            Analyzer.totalExecution.stop();
            Analyzer.write();
            Analyzer = null;
        }

        return lpmResult;
    }

    public static LPMResult run(Set<Place> places, XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        setUpAnalyzer(log, false);
        LPMResult lpmResult;

        Analyzer.totalExecution.start();

        try {
            lpmResult = Main.discover(places, log, parameters);
        } finally {
            Analyzer.totalExecution.stop();
            Analyzer.write();
            Analyzer = null;
        }

        return lpmResult;
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
                    }
                }, parameters.getTimeLimit()
        );

        // places = PlaceDiscovery.filterPlaces(places);

        Analyzer.lpmDiscoveryExecution.start();
        LPMResult result = new LPMResult();
        try {
            // analyze log
            LogAnalyzer logAnalyzer = new LogAnalyzer(log, new LogAnalyzerParameters(parameters.getLpmCombinationParameters().getLpmProximity()));
            LEFRMatrix lefrMatrix = logAnalyzer.getLEFRMatrix();
            Main.getContext().getProvidedObjectManager()
                    .createProvidedObject("LEFR - " + parameters.getPlaceDiscoveryAlgorithmId() + " from: "
                                    + log.getAttributes().get("concept:name"), lefrMatrix, LEFRMatrix.class,
                            Main.getContext());
            ProvidedObjectHelper.setFavorite(Main.getContext(), lefrMatrix);

            // choose places
            parameters.getPlaceChooserParameters()
                    .setFollowRelationsLimit(parameters.getLpmCombinationParameters().getLpmProximity());
            PlaceChooser placeChooser = new PlaceChooser(parameters.getPlaceChooserParameters(), places, log, lefrMatrix);
            places = placeChooser.choose();

            // export chosen places
            PlaceSet placeSet = new PlaceSet(places);
            placeSet.writePassageUsage(logAnalyzer.getLEFRMatrix());
            Main.getContext().getProvidedObjectManager()
                    .createProvidedObject("Chosen Place Set - " + parameters.getPlaceDiscoveryAlgorithmId() + " from: "
                            + log.getAttributes().get("concept:name"), placeSet, PlaceSet.class, Main.getContext());
            ProvidedObjectHelper.setFavorite(Main.getContext(), placeSet);

            // setup the combination controller
            LPMCombinationController controller = new LPMCombinationController(parameters.getLpmCombinationParameters());

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

            Analyzer.setCountPlacesUsed(places.size());

            result.addAll(controller.combineUsingFPGrowth(places, log,
                    parameters.getLpmCombinationParameters().getLpmProximity(), parameters.getLpmCount()));

            Analyzer.setAllLpmDiscovered(result.size());
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
                result.sort((LocalProcessModel lpm1, LocalProcessModel lpm2) ->
                        lpm1.getAdditionalInfo().getEvaluationResult().getResult(aggregateOperation)
                                < lpm2.getAdditionalInfo().getEvaluationResult().getResult(aggregateOperation));
                result.keep(parameters.getLpmCount());
            }
        } finally {
            Analyzer.setLpmDiscovered(result.size());

            Analyzer.lpmDiscoveryExecution.stop();

            timer.cancel();
        }

        return result;
    }
}
