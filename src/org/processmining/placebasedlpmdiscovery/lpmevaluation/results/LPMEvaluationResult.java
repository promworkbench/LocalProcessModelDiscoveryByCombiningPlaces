package org.processmining.placebasedlpmdiscovery.lpmevaluation.results;

import java.io.Serializable;

public interface LPMEvaluationResult extends Serializable, Cloneable {

    double getResult();

    double getNormalizedResult();

    LPMEvaluationResultId getId();
}
