package org.processmining.placebasedlpmdiscovery.lpmdistances.mixed;

import org.apache.commons.lang3.tuple.Pair;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

import java.util.List;

public class MixedModelDistanceConfig implements ModelDistanceConfig {

    public static final String METHOD = "Mixed";
    @Override
    public String getDistanceMethod() {
        return METHOD;
    }

    public List<Pair<ModelDistance, Double>> getModelDistanceWeightPairs() {
        return null;
    }
}
