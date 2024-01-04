package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.deckfour.xes.model.XTrace;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMCollectorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.CaseAttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummaryController;

public class CaseAttributeCollector implements WindowLPMCollector<CaseAttributeCollectorResult> {
    @Override
    public CaseAttributeCollectorResult evaluate(LocalProcessModel lpm,
                                                  LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                                  LPMEvaluationResult existingEvaluation) {

        if (!(existingEvaluation instanceof CaseAttributeCollectorResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    CaseAttributeCollectorResult.class);
        }

        CaseAttributeCollectorResult result = (CaseAttributeCollectorResult) existingEvaluation;

        AttributeSummaryController attrSummaryController = new AttributeSummaryController();

        for (XTrace trace : lpmTemporaryWindowInfo.getOriginalTraces()) {
            attrSummaryController.computeAttributeSummary(result, trace);
        }
        return result;
    }

    @Override
    public String getKey() {
        return StandardLPMCollectorId.CaseAttributeCollector.name();
    }

    @Override
    public String getResultKey() {
        return StandardLPMCollectorResultId.CaseAttributeCollectorResult.name();
    }

    @Override
    public CaseAttributeCollectorResult createEmptyResult(LocalProcessModel lpm) {
        return new CaseAttributeCollectorResult();
    }

    @Override
    public Class<CaseAttributeCollectorResult> getResultClass() {
        return CaseAttributeCollectorResult.class;
    }
}
