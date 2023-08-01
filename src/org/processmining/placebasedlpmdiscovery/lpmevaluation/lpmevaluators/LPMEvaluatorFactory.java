package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete.*;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;

public class LPMEvaluatorFactory {

    public LPMEvaluator<? extends LPMEvaluationResult> getEvaluator(LPMEvaluatorId evaluatorId) {
        if (evaluatorId == LPMEvaluatorId.TransitionOverlappingEvaluator)
            return new TransitionsOverlappingEvaluator();
        if (evaluatorId == LPMEvaluatorId.FittingWindowEvaluator)
            return null;
        if (evaluatorId == LPMEvaluatorId.PassageRepetitionEvaluator)
            return new PassageRepetitionEvaluator();

        throw new IllegalArgumentException("Evaluator with id " + evaluatorId + " does not exist.");
    }

    public WindowLPMEvaluator<? extends LPMEvaluationResult> getWindowEvaluator(LPMEvaluatorId evaluatorId) {
        if (evaluatorId == LPMEvaluatorId.PassageCoverageEvaluator)
            return new PassageCoverageEvaluator();
        if (evaluatorId == LPMEvaluatorId.FittingWindowEvaluator)
            return new FittingWindowEvaluator();
        if (evaluatorId == LPMEvaluatorId.TraceSupportCountEvaluator)
            return new TraceSupportEvaluator();
        if (evaluatorId == LPMEvaluatorId.TransitionCoverageEvaluator)
            return new TransitionCoverageEvaluator();
        throw new IllegalArgumentException("Window evaluator with id " + evaluatorId + " does not exist.");
    }
}
