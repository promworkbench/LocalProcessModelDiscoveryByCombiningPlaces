package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.AbstractEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryInfo;

public interface WindowLPMEvaluator<T extends AbstractEvaluationResult> {

    /**
     * Evaluates a local process model given the parameters of the evaluator
     *
     * @param lpm: the local process model that needs to be evaluated
     * @return the result of the evaluation
     */
//    T evaluate(LocalProcessModel lpm, LPMTemporaryInfo lpmTemporaryInfo, T existingEvaluation);
    T evaluate(LocalProcessModel lpm, LPMTemporaryInfo lpmTemporaryInfo, AbstractEvaluationResult existingEvaluation);

    String getKey();

    T createEmptyInfo();

    Class<T> getResultClass();
}
