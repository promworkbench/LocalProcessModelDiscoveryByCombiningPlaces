package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete.PassageRepetitionEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete.TransitionsOverlappingEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.AbstractEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;

public class LPMEvaluatorFactory {

    public LPMEvaluator<? extends LPMEvaluationResult> getEvaluator(LPMEvaluatorId evaluatorId) {
        if (evaluatorId == LPMEvaluatorId.TransitionOverlappingEvaluator)
            return new TransitionsOverlappingEvaluator();
        if (evaluatorId == LPMEvaluatorId.WindowEvaluator)
            return null;
        if (evaluatorId == LPMEvaluatorId.PassageRepetitionEvaluator)
            return new PassageRepetitionEvaluator();
        throw new IllegalArgumentException("Evaluator with id " + evaluatorId + " does not exist.");
    }
}
