package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms;

import org.processmining.placebasedlpmdiscovery.RunningContext;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlg;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.LPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals.LPMBuildingResultTraversal;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals.LPMBuildingResultTraversalFactory;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.discovery.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.interruptible.InterrupterSubject;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class StandardLPMDiscoveryAlg implements LPMDiscoveryAlg {

    private final RunningContext runningContext;
    private final PlaceBasedLPMDiscoveryParameters parameters;
    private final LPMBuildingAlg lpmBuildingAlg;
    private LPMFiltrationController lpmFiltrationController;

    public StandardLPMDiscoveryAlg(RunningContext runningContext,
                                   PlaceBasedLPMDiscoveryParameters parameters,
                                   LPMBuildingAlg lpmBuildingAlg,
                                   LPMFiltrationController lpmFiltrationController) {
        this.runningContext = runningContext;
        this.parameters = parameters;
        this.lpmBuildingAlg = lpmBuildingAlg;
        this.lpmFiltrationController = lpmFiltrationController;
    }

    @Override
    public LPMDiscoveryResult run(LPMDiscoveryInput input) {
        // cast input


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
        LPMDiscoveryResult result;
        try {
            // analyze log
            this.runningContext.getAnalyzer().logAnalyzer.analyze(parameters.getLpmCombinationParameters().getLpmProximity());
//            LEFRMatrix lefrMatrix = this.runningContext.getAnalyzer().logAnalyzer.getLEFRMatrix(parameters
//            .getLpmCombinationParameters().getLpmProximity());

//            // discover places
//            Set<Place> places = this.placeDiscovery.getPlaces().getPlaces();
//            this.runningContext.getAnalyzer().getStatistics().getParameterStatistics().setPlaceDiscoveryIncluded
//            (true);
//
//            // choose places
//            parameters.getPlaceChooserParameters()
//                    .setFollowRelationsLimit(parameters.getLpmCombinationParameters().getLpmProximity());
//            places = this.placeChooser.choose(places, parameters.getPlaceChooserParameters().getPlaceLimit());
//
//            // export chosen places
//            PlaceSet placeSet = new PlaceSet(places);
//            placeSet.writePassageUsage(this.runningContext.getAnalyzer().logAnalyzer.getLEFRMatrix(parameters
//            .getLpmCombinationParameters().getLpmProximity()));

            // build lpms
            LPMBuildingResult lpmBuildingResult = this.lpmBuildingAlg.build(input.getLPMBuildingInput(),
                    parameters.getLPMBuildingParameters());

            // choose lpms
            Set<LocalProcessModel> lpms = new HashSet<>();
            LPMBuildingResultTraversal traversal = LPMBuildingResultTraversalFactory.createTraversal(lpmBuildingResult);
            while (traversal.hasNext()) {
                LocalProcessModel lpm = traversal.next();
                if (this.lpmFiltrationController.shouldKeepLPM(lpm)) {
                    lpms.add(lpm);
                }
            }
            result = new StandardLPMDiscoveryResult(lpms);
//            result = this.lpmCombination.combine(places, parameters.getLpmCount());
            result.keep(parameters.getLpmCount());
            result.setInput(input);

            this.runningContext.getAnalyzer().logAllLpmDiscovered(result.getAllLPMs().size());

            // TODO: Normalization has to be moved to another place
//            if (result.size() > 0) {
//                // normalize the fitting windows score
//                double max = result.highestScoringElement((LocalProcessModel lpm) -> lpm.getAdditionalInfo()
//                                .getEvaluationResult(StandardLPMEvaluationResultId.FittingWindowsEvaluationResult
//                                .name(),
//                                        FittingWindowsEvaluationResult.class).getResult())
//                        .getAdditionalInfo().getEvaluationResult(
//                                StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
//                                FittingWindowsEvaluationResult.class).getResult();
//                result.edit(lpm -> ((FittingWindowsEvaluationResult) lpm.getAdditionalInfo()
//                        .getEvaluationResult(StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
//                                FittingWindowsEvaluationResult.class))
//                        .normalizeResult(max, 0));
//
//                EvaluationResultAggregateOperation aggregateOperation = new EvaluationResultAggregateOperation();
//                result.sort((LocalProcessModel lpm1, LocalProcessModel lpm2) -> Double.compare(
//                        aggregateOperation.aggregate(lpm1.getAdditionalInfo().getEvalResults().values()),
//                        aggregateOperation.aggregate(lpm2.getAdditionalInfo().getEvalResults().values())));
//                result.keep(parameters.getLpmCount());
//            }
        } finally {
            this.runningContext.getAnalyzer().logLpmReturned(parameters.getLpmCount());
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
