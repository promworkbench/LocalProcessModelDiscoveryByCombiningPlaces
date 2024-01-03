package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete.*;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;

public class LPMCollectorFactory {

    public LPMEvaluator<? extends LPMEvaluationResult> getEvaluator(LPMEvaluatorId evaluatorId) {
        if (evaluatorId == StandardLPMEvaluatorId.TransitionOverlappingEvaluator)
            return new TransitionsOverlappingEvaluator();
        if (evaluatorId == StandardLPMEvaluatorId.FittingWindowEvaluator)
            return null;
        if (evaluatorId == StandardLPMEvaluatorId.PassageRepetitionEvaluator)
            return new PassageRepetitionEvaluator();

        throw new IllegalArgumentException("Evaluator with id " + evaluatorId + " does not exist.");
    }

    public WindowLPMCollector<? extends LPMCollectorResult> getWindowEvaluator(LPMCollectorId collectorId) {
        if (collectorId == StandardLPMEvaluatorId.PassageCoverageEvaluator)
            return new PassageCoverageCollector();
        if (collectorId == StandardLPMEvaluatorId.FittingWindowEvaluator)
            return new FittingWindowCollector();
        if (collectorId == StandardLPMEvaluatorId.TraceSupportCountEvaluator)
            return new TraceSupportCollector();
        if (collectorId == StandardLPMEvaluatorId.TransitionCoverageEvaluator)
            return new TransitionCoverageCollector();
        if (collectorId == StandardLPMEvaluatorId.EventCoverageEvaluator)
            return new EventCoverageCollector();
        if (collectorId == StandardLPMCollectorId.EventAttributeEvaluator)
            return new EventAttributeCollector();
        throw new IllegalArgumentException("Window evaluator with id " + collectorId + " does not exist.");
    }
}
