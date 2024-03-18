package org.processmining.placebasedlpmdiscovery.lpmdistances.mixed;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

public class WeightedModelDistanceConfig {
    private final String key;
    private final ModelDistanceConfig distanceConfig;
    private final Double weight;

    public WeightedModelDistanceConfig(String key, ModelDistanceConfig distanceConfig, Double weight) {
        this.key = key;
        this.distanceConfig = distanceConfig;
        this.weight = weight;
    }

    public ModelDistanceConfig getDistanceConfig() {
        return distanceConfig;
    }

    public Double getWeight() {
        return weight;
    }

    public String getKey() {
        return key;
    }
}
