package org.processmining.placebasedlpmdiscovery.lpmevaluation.results;

import java.io.Serializable;

public interface LPMEvaluationResult extends LPMCollectorResult {

    double getResult();

    double getNormalizedResult();

}
