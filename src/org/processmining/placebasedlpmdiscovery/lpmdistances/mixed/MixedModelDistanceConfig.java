package org.processmining.placebasedlpmdiscovery.lpmdistances.mixed;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

import java.util.Collection;

public class MixedModelDistanceConfig implements ModelDistanceConfig {

    public static final String METHOD = "Mixed";

    private final Collection<WeightedModelDistanceConfig> includedDistancesConfigs;

    public MixedModelDistanceConfig(Collection<WeightedModelDistanceConfig> includedDistancesConfigs) {
        this.includedDistancesConfigs = includedDistancesConfigs;
    }

    @Override
    public String getDistanceMethod() {
        return METHOD;
    }

    public Collection<WeightedModelDistanceConfig> getModelDistanceWeightPairs() {
        return includedDistancesConfigs;
    }
}
