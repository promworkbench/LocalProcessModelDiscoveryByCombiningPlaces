package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class TransitionsOverlappingEvaluationResult extends SimpleEvaluationResult {
    private static final long serialVersionUID = 6115876525425602476L;

    private double overlappingScore;

    public TransitionsOverlappingEvaluationResult(LocalProcessModel lpm) {
        super(lpm, StandardLPMEvaluationResultId.TransitionOverlappingEvaluationResult);
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
