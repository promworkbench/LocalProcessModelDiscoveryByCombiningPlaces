package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.AbstractEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public interface LPMEvaluator<T extends LPMEvaluationResult> {

    /**
     * Evaluates a local process model
     *
     * @param lpm: the local process model that needs to be evaluated
     * @return the result of the evaluation
     */
    T evaluate(LocalProcessModel lpm);
}
