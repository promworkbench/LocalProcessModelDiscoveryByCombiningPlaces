package org.processmining.placebasedlpmdiscovery.evaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.evaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.evaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class TransitionsOverlappingEvaluationResult extends SimpleEvaluationResult {
    private static final long serialVersionUID = 6115876525425602476L;

    private double overlappingScore;

    public TransitionsOverlappingEvaluationResult(LocalProcessModel lpm) {
        super(lpm);
    }

    @Override
    public LPMEvaluationResultId getId() {
        return LPMEvaluationResultId.TransitionOverlappingEvaluationResult;
    }

    public void setOverlappingScore(double overlappingScore) {
        this.overlappingScore = overlappingScore;
    }

    @Override
    public double getResult() {
        return overlappingScore;
    }

    @Override
    public double getNormalizedResult() {
        return this.getResult();
    }
}
