package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public interface WindowLPMCollector<T extends LPMCollectorResult> {

    /**
     * Evaluates a local process model given the parameters of the evaluator
     *
     * @param lpm: the local process model that needs to be evaluated
     * @return the result of the evaluation
     */
//    T evaluate(LocalProcessModel lpm, LPMTemporaryInfo lpmTemporaryInfo, T existingEvaluation);
    T evaluate(LocalProcessModel lpm, LPMTemporaryWindowInfo lpmTemporaryWindowInfo, LPMCollectorResult existingEvaluation);

    String getKey();

    String getResultKey();

    T createEmptyResult(LocalProcessModel lpm);

    Class<T> getResultClass();
}
