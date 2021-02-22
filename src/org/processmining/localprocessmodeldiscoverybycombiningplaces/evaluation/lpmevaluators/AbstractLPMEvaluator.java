package org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.AbstractEvaluationResult;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;

public abstract class AbstractLPMEvaluator<T extends AbstractEvaluationResult> {

    /**
     * Evaluates a local process model given the parameters of the evaluator
     *
     * @param lpm: the local process model that needs to be evaluated
     * @return: the result of the evaluation
     */
    public abstract T evaluate(LocalProcessModel lpm);
}
