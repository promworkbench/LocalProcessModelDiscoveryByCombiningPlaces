package org.processmining.placebasedlpmdiscovery.lpmdistances;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.List;
import java.util.Map;

public class ModelDistanceController {

    private final ModelDistance modelDistance;

    @Inject
    public ModelDistanceController(ModelDistanceFactory modelDistanceFactory,
                                   ModelDistanceConfig modelDistanceConfig) {
        this.modelDistance = modelDistanceFactory.getModelDistance(modelDistanceConfig);
    }

    public double[][] getDistanceMatrix(List<LocalProcessModel> lpmList) {
        return modelDistance.calculatePairwiseDistance(lpmList);
    }
}
