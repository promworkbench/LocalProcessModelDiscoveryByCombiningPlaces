package org.processmining.placebasedlpmdiscovery.lpmdistances.mixed;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

import java.util.List;

public class MixedModelDistanceConfig implements ModelDistanceConfig {

    public static final String METHOD = "Mixed";

    private final List<WeightedModelDistanceConfig> includedDistancesConfigs;

    public MixedModelDistanceConfig(List<WeightedModelDistanceConfig> includedDistancesConfigs) {
        this.includedDistancesConfigs = includedDistancesConfigs;
    }

    @Override
    public String getDistanceMethod() {
        return METHOD;
    }

    public List<WeightedModelDistanceConfig> getModelDistanceWeightPairs() {
        return includedDistancesConfigs;
    }
}
