package org.processmining.placebasedlpmdiscovery.evaluation.lpmevaluators;

import org.processmining.placebasedlpmdiscovery.evaluation.results.AbstractEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public abstract class AbstractLPMEvaluator<T extends AbstractEvaluationResult> {

    /**
     * Evaluates a local process model given the parameters of the evaluator
     *
     * @param lpm: the local process model that needs to be evaluated
     * @return: the result of the evaluation
     */
    public abstract T evaluate(LocalProcessModel lpm);
}
