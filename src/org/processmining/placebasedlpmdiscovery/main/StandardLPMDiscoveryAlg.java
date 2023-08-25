package org.processmining.placebasedlpmdiscovery.main;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.LPMCombinationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.interruptible.InterrupterSubject;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class StandardLPMDiscoveryAlg implements LPMDiscoveryAlg {

    private final RunningContext runningContext;
    private final PlaceBasedLPMDiscoveryParameters parameters;

    private final PlaceChooser placeChooser;
    private final PlaceDiscovery placeDiscovery;

    private final LPMCombinationController lpmCombination;

    StandardLPMDiscoveryAlg(RunningContext runningContext,
                            PlaceBasedLPMDiscoveryParameters parameters,
                            PlaceDiscovery placeDiscovery,
                            PlaceChooser placeChooser,
                            LPMCombinationController lpmCombination) {
        this.runningContext = runningContext;
        this.parameters = parameters;
        this.placeChooser = placeChooser;
        this.placeDiscovery = placeDiscovery;
        this.lpmCombination = lpmCombination;
    }

    @Override
    public LPMDiscoveryResult run() {
        this.runningContext.getAnalyzer().totalExecution.start();

        // create task that will broadcast interrupt event when the time limit is met
        InterrupterSubject interrupterSubject = new InterrupterSubject();
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        interrupterSubject.notifyInterruption();
                        runningContext.getAnalyzer().getStatistics().getGeneralStatistics().setInterrupted(true);
                        System.out.println("INTERRUPTED");
                    }
                }, parameters.getTimeLimit()
        );
        this.runningContext.setInterrupterSubject(interrupterSubject);

        // setup statistics for the parameters
        this.runningContext.getAnalyzer().setStatisticsForParameters(parameters);

        // places = PlaceDiscovery.filterPlaces(places);

        this.runningContext.getAnalyzer().lpmDiscoveryExecution.start();
        LPMResult result = new LPMResult();
        try {
            // analyze log
            this.runningContext.getAnalyzer().logAnalyzer.analyze(parameters.getLpmCombinationParameters().getLpmProximity());
            LEFRMatrix lefrMatrix = this.runningContext.getAnalyzer().logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity());

            // discover places
            Set<Place> places = this.placeDiscovery.getPlaces().getPlaces();
            this.runningContext.getAnalyzer().getStatistics().getParameterStatistics().setPlaceDiscoveryIncluded(true);

            // choose places
            parameters.getPlaceChooserParameters()
                    .setFollowRelationsLimit(parameters.getLpmCombinationParameters().getLpmProximity());
            places = this.placeChooser.choose(places, parameters.getPlaceChooserParameters().getPlaceLimit());

            // export chosen places
            PlaceSet placeSet = new PlaceSet(places);
            placeSet.writePassageUsage(this.runningContext.getAnalyzer().logAnalyzer.getLEFRMatrix(parameters.getLpmCombinationParameters().getLpmProximity()));

            this.runningContext.getAnalyzer().logCountPlacesUsed(places.size());

            result.addAll(this.lpmCombination.combine(places, parameters.getLpmCount()));

            this.runningContext.getAnalyzer().logAllLpmDiscovered(result.size());

            if (result.size() > 0) {
                // normalize the fitting windows score
                double max = result.highestScoringElement((LocalProcessModel lpm) -> lpm.getAdditionalInfo()
                                .getEvaluationResult(StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                                        FittingWindowsEvaluationResult.class).getResult())
                        .getAdditionalInfo().getEvaluationResult(
                                StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                                FittingWindowsEvaluationResult.class).getResult();
                result.edit(lpm -> ((FittingWindowsEvaluationResult) lpm.getAdditionalInfo()
                        .getEvaluationResult(StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                                FittingWindowsEvaluationResult.class))
                        .normalizeResult(max, 0));

                EvaluationResultAggregateOperation aggregateOperation = new EvaluationResultAggregateOperation();
                result.sort((LocalProcessModel lpm1, LocalProcessModel lpm2) -> Double.compare(
                        LocalProcessModelUtils.getGroupedEvaluationResult(lpm1).getResult(aggregateOperation),
                        LocalProcessModelUtils.getGroupedEvaluationResult(lpm2).getResult(aggregateOperation)));
                result.keep(parameters.getLpmCount());
            }
        } finally {
            this.runningContext.getAnalyzer().logLpmReturned(result.size());
            this.runningContext.getAnalyzer().lpmDiscoveryExecution.stop();
            this.runningContext.getAnalyzer().close();
            this.runningContext.getAnalyzer().totalExecution.stop();
            this.runningContext.getAnalyzer().write();
            this.runningContext.setAnalyzer(null);
            timer.cancel();
        }

        return result;
    }
}
