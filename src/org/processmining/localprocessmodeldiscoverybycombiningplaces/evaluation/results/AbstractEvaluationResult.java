package org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;

import java.io.Serializable;

public abstract class AbstractEvaluationResult implements Cloneable, Serializable {

    private static final long serialVersionUID = 4500146576771470365L;

    protected LocalProcessModel lpm; // the object state should not be changed

    public AbstractEvaluationResult(LocalProcessModel lpm) {
        this.lpm = lpm;
    }

    @Override
    public AbstractEvaluationResult clone() throws CloneNotSupportedException {
        return (AbstractEvaluationResult) super.clone();
    }
}
