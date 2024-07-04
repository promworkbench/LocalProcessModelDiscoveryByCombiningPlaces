package org.processmining.placebasedlpmdiscovery.lpmdistances;

import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;

import java.util.Map;

public class ModelDistanceUtils {
    public static ModelDistanceConfig createModelDistanceConfig(Map<String, Object> parameters) {
        return new DataAttributeModelDistanceConfig();
    }
}
