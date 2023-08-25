package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete.*;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;

public class LPMEvaluatorFactory {

    public LPMEvaluator<? extends LPMEvaluationResult> getEvaluator(LPMEvaluatorId evaluatorId) {
        if (evaluatorId == StandardLPMEvaluatorId.TransitionOverlappingEvaluator)
            return new TransitionsOverlappingEvaluator();
        if (evaluatorId == StandardLPMEvaluatorId.FittingWindowEvaluator)
            return null;
        if (evaluatorId == StandardLPMEvaluatorId.PassageRepetitionEvaluator)
            return new PassageRepetitionEvaluator();

        throw new IllegalArgumentException("Evaluator with id " + evaluatorId + " does not exist.");
    }

    public WindowLPMEvaluator<? extends LPMEvaluationResult> getWindowEvaluator(LPMEvaluatorId evaluatorId) {
        if (evaluatorId == StandardLPMEvaluatorId.PassageCoverageEvaluator)
            return new PassageCoverageEvaluator();
        if (evaluatorId == StandardLPMEvaluatorId.FittingWindowEvaluator)
            return new FittingWindowEvaluator();
        if (evaluatorId == StandardLPMEvaluatorId.TraceSupportCountEvaluator)
            return new TraceSupportEvaluator();
        if (evaluatorId == StandardLPMEvaluatorId.TransitionCoverageEvaluator)
            return new TransitionCoverageEvaluator();
        throw new IllegalArgumentException("Window evaluator with id " + evaluatorId + " does not exist.");
    }
}
