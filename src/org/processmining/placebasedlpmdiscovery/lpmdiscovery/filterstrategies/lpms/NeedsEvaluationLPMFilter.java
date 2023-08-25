package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMEvaluatorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;

public abstract class NeedsEvaluationLPMFilter extends AbstractLPMFilter {

    @Override
    public boolean needsEvaluation() {
        return true;
    }

    public abstract LPMEvaluatorId getEvaluatorId();

    public abstract LPMEvaluationResultId getEvaluationId();
}
