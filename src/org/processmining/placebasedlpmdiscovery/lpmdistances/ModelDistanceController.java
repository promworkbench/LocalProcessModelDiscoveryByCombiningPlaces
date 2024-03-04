package org.processmining.placebasedlpmdiscovery.lpmdistances;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.List;
import java.util.Map;

public class ModelDistanceController {

    private ModelDistance modelDistance;

    public ModelDistanceController(String distanceMethod, Map<String, String> distanceConfig) {

    }

    public double[][] getDistanceMatrix(List<LocalProcessModel> lpmList) {
        return modelDistance.calculatePairwiseDistance(lpmList);
    }
}
