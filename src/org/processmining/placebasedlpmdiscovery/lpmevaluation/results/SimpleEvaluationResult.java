package org.processmining.placebasedlpmdiscovery.lpmevaluation.results;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public abstract class SimpleEvaluationResult implements LPMEvaluationResult {

    private static final long serialVersionUID = 8123973260828398630L;
    private final LPMEvaluationResultId id;
//    protected transient LocalProcessModel lpm;

    public SimpleEvaluationResult(LPMEvaluationResultId id) {
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
