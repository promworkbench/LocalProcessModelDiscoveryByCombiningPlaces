package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.StandardLPMCollectorId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.PlaceVisitsCollectorResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

public class PlaceVisitsCollector implements WindowLPMCollector<PlaceVisitsCollectorResult> {
    @Override
    public PlaceVisitsCollectorResult evaluate(LocalProcessModel lpm,
                                               LPMTemporaryWindowInfo lpmTemporaryWindowInfo,
                                               LPMCollectorResult existingEvaluation) {

        if (!(existingEvaluation instanceof PlaceVisitsCollectorResult)) {
            throw new IllegalArgumentException("The passed evaluation result should be of type " +
                    PlaceVisitsCollectorResult.class);
        }

        PlaceVisitsCollectorResult result = (PlaceVisitsCollectorResult) existingEvaluation;
        result.updateUsedPlaces(lpmTemporaryWindowInfo.getUsedPlaces(), lpmTemporaryWindowInfo.getWindowCount());
        return result;
    }

    @Override
    public String getKey() {
        return StandardLPMCollectorId.PlaceVisitsCollector.name();
    }

    @Override
    public String getResultKey() {
        return StandardLPMCollectorResultId.PlaceVisitsCollectorResult.name();
    }

    @Override
    public PlaceVisitsCollectorResult createEmptyResult(LocalProcessModel lpm) {
        return new PlaceVisitsCollectorResult(lpm);
    }

    @Override
    public Class<PlaceVisitsCollectorResult> getResultClass() {
        return PlaceVisitsCollectorResult.class;
    }
}
