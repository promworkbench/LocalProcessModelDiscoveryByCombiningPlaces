package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMCollectorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.PassageVisitsCollectorResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

import java.util.List;

public class PassageVisitsCollector implements WindowLPMCollector<PassageVisitsCollectorResult> {
    @Override
    public PassageVisitsCollectorResult evaluate(LocalProcessModel lpm, LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                                 LPMCollectorResult existingEvaluation) {
        if (!(existingEvaluation instanceof PassageVisitsCollectorResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    PassageVisitsCollectorResult.class);
        }

        PassageVisitsCollectorResult result = (PassageVisitsCollectorResult) existingEvaluation;

        int windowCount = lpmTemporaryWindowInfo.getWindowCount();
        List<Integer> firingSequence = lpmTemporaryWindowInfo.getIntegerFiringSequence();
        result.updatePassagesUsed(lpmTemporaryWindowInfo.getIntegerUsedPassages(), windowCount);
        result.updateFirstPassage(firingSequence.get(0), windowCount);
        result.updateLastPassage(firingSequence.get(firingSequence.size() - 1), windowCount);
        return result;
    }

    @Override
    public String getKey() {
        return StandardLPMCollectorId.PassageVisitsCollector.name();
    }

    @Override
    public String getResultKey() {
        return StandardLPMCollectorResultId.PassageVisitsCollectorResult.name();
    }

    @Override
    public PassageVisitsCollectorResult createEmptyResult(LocalProcessModel lpm) {
        return new PassageVisitsCollectorResult(lpm);
    }

    @Override
    public Class<PassageVisitsCollectorResult> getResultClass() {
        return PassageVisitsCollectorResult.class;
    }
}
