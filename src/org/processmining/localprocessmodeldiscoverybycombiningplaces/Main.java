package org.processmining.localprocessmodeldiscoverybycombiningplaces;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators.LPMEvaluatorFactory;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.loganalyzer.LEFRMatrix;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.loganalyzer.LogAnalyzer;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.loganalyzer.LogAnalyzerParameters;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.combination.LPMCombinationController;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.combination.guards.complex.AndCombinationGuard;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.combination.guards.simple.NotContainingCoveringPlacesCombinationGuard;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.combination.guards.simple.SameActivityCombinationGuard;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.LPMFilterFactory;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.LPMFilterParameters;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.lpms.LPMFilter;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.lpms.LPMFilterId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.InterrupterSubject;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.Place;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.serializable.LPMResult;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.serializable.PlaceSet;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placechooser.PlaceChooser;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.PlaceDiscovery;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.PlaceDiscoveryResult;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.plugins.utils.ProvidedObjectHelper;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.utils.analysis.Analyzer;

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
        setUpAnalyzer(log, false);
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
            Analyzer.write("analysis", false);
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
            Analyzer.write("analysis", false);
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

        // analyze log
        LogAnalyzer logAnalyzer = new LogAnalyzer(log, new LogAnalyzerParameters());
        logAnalyzer.calculateLEFRMatrix();
        LEFRMatrix lefrMatrix = logAnalyzer.getLEFRMatrix();
        Main.getContext().getProvidedObjectManager()
                .createProvidedObject("LEFR - " + parameters.getPlaceDiscoveryAlgorithmId() + " from: "
                                + log.getAttributes().get("concept:name"), lefrMatrix, LEFRMatrix.class,
                        Main.getContext());
        ProvidedObjectHelper.setFavorite(Main.getContext(), lefrMatrix);

        // choose places
        parameters.getPlaceChooserParameters()
                .setFollowRelationsLimit(parameters.getLpmCombinationParameters().getLpmProximity());
        PlaceChooser placeChooser = new PlaceChooser(parameters.getPlaceChooserParameters(), places, log);
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
        LPMFiltrationController filtrationController = new LPMFiltrationController();
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
        LPMResult result = new LPMResult();

        result.addAll(controller.combineUsingFPGrowth(places, log,
                parameters.getLpmCombinationParameters().getLpmProximity(), parameters.getLpmCount()));


        EvaluationResultAggregateOperation aggregateOperation = new EvaluationResultAggregateOperation();
        result.sort((LocalProcessModel lpm1, LocalProcessModel lpm2) ->
                lpm1.getAdditionalInfo().getEvaluationResult().getResult(aggregateOperation)
                        < lpm2.getAdditionalInfo().getEvaluationResult().getResult(aggregateOperation));
        result.keep(parameters.getLpmCount());
        Analyzer.setLpmDiscovered(result.size());

        Analyzer.lpmDiscoveryExecution.stop();
        return result;
    }
}
