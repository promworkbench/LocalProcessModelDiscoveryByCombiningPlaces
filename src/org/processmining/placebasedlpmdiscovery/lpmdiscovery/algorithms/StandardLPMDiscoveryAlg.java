package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlg;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.LPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals.LPMBuildingResultTraversal;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.traversals.LPMBuildingResultTraversalFactory;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.parameters.LPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.discovery.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.interruptible.InterrupterSubject;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

public class StandardLPMDiscoveryAlg implements LPMDiscoveryAlg {

    private final LPMBuildingAlg lpmBuildingAlg;
    private final LPMFiltrationController lpmFiltrationController;
    private final LPMEvaluationController evaluationController;

    public StandardLPMDiscoveryAlg(LPMBuildingAlg lpmBuildingAlg,
                                   LPMFiltrationController lpmFiltrationController,
                                   LPMEvaluationController evaluationController) {
        this.lpmBuildingAlg = lpmBuildingAlg;
        this.lpmFiltrationController = lpmFiltrationController;
        this.evaluationController = evaluationController;
    }

    @Override
    public LPMDiscoveryResult run(LPMDiscoveryInput input, LPMDiscoveryParameters parameters) {
        // create task that will broadcast interrupt event when the time limit is met
        InterrupterSubject interrupterSubject = new InterrupterSubject();
        Timer timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        interrupterSubject.notifyInterruption();
                        System.out.println("INTERRUPTED");
                    }
                }, parameters.getTimeLimit()
        );

        // places = PlaceDiscovery.filterPlaces(places);

        LPMDiscoveryResult result;
        try {
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
            result.addAdditionalResults("eventCoverageSetLevel", this.evaluationController.getEventCoverageSetLevel());
//            result = this.lpmCombination.combine(places, parameters.getLpmCount());
            result.keep(parameters.getLpmCount());
            result.setInput(input);

            // post-process lpms



        } finally {
            timer.cancel();
        }

        return result;
    }
}
