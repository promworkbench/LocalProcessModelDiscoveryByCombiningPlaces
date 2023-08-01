package org.processmining.placebasedlpmdiscovery.lpmevaluation.results;

public interface LPMEvaluationResult {

    double getResult();

    double getNormalizedResult();

    LPMEvaluationResultId getId();
}
