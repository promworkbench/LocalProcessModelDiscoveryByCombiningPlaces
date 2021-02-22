package org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators.concrete.PassageRepetitionEvaluator;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators.concrete.TransitionsOverlappingEvaluator;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.AbstractEvaluationResult;

public class LPMEvaluatorFactory {

    public AbstractLPMEvaluator<? extends AbstractEvaluationResult> getEvaluator(LPMEvaluatorId evaluatorId) {
        if (evaluatorId == LPMEvaluatorId.TransitionOverlappingEvaluator)
            return new TransitionsOverlappingEvaluator();
        if (evaluatorId == LPMEvaluatorId.WindowEvaluator)
            return null;
        if (evaluatorId == LPMEvaluatorId.PassageRepetitionEvaluator)
            return new PassageRepetitionEvaluator();
        throw new IllegalArgumentException("Evaluator with id " + evaluatorId + " does not exist.");
    }
}
