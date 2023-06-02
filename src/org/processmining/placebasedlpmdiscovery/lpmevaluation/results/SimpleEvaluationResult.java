package org.processmining.placebasedlpmdiscovery.lpmevaluation.results;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public abstract class SimpleEvaluationResult extends AbstractEvaluationResult {

    private static final long serialVersionUID = 8123973260828398630L;

    private final LPMEvaluationResultId id;

    public SimpleEvaluationResult(LocalProcessModel lpm, LPMEvaluationResultId id) {
        super(lpm);
        this.id = id;
    }

    public LPMEvaluationResultId getId() {
        return this.id;
    }

    public abstract double getResult();

    public abstract double getNormalizedResult();

    @Override
    public SimpleEvaluationResult clone() throws CloneNotSupportedException {
        return (SimpleEvaluationResult) super.clone();
    }
}
