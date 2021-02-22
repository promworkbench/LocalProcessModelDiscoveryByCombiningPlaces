package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.filterstrategies.lpms;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.lpmevaluators.LPMEvaluatorId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.LPMEvaluationResultId;

public abstract class NeedsEvaluationLPMFilter extends AbstractLPMFilter {

    @Override
    public boolean needsEvaluation() {
        return true;
    }

    public abstract LPMEvaluatorId getEvaluatorId();

    public abstract LPMEvaluationResultId getEvaluationId();
}
