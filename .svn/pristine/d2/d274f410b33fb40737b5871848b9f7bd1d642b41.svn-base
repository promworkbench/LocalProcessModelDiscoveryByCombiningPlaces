package org.processmining.placebasedlpmdiscovery.evaluation.results;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public abstract class SimpleEvaluationResult extends AbstractEvaluationResult {

    private static final long serialVersionUID = 8123973260828398630L;

    public SimpleEvaluationResult(LocalProcessModel lpm) {
        super(lpm);
    }

    public abstract LPMEvaluationResultId getId();

    public abstract double getResult();

    public abstract double getNormalizedResult();

    @Override
    public SimpleEvaluationResult clone() throws CloneNotSupportedException {
        return (SimpleEvaluationResult) super.clone();
    }
}
