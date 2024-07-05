package org.processmining.placebasedlpmdiscovery.lpmdistances;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.List;

public class ModelDistanceController {

    // services
    private final ModelDistanceFactory modelDistanceFactory;

    @Inject
    public ModelDistanceController(ModelDistanceFactory modelDistanceFactory) {
        this.modelDistanceFactory = modelDistanceFactory;
    }

    public double[][] getDistanceMatrix(List<LocalProcessModel> lpmList, ModelDistanceConfig modelDistanceConfig) {
        ModelDistance modelDistance = this.modelDistanceFactory.getModelDistance(modelDistanceConfig);
        return modelDistance.calculatePairwiseDistance(lpmList);
    }
}
