package org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

public class DataAttributeModelDistanceConfig implements ModelDistanceConfig {

    public static final String METHOD = "DataAttribute";
    @Override
    public String getDistanceMethod() {
        return METHOD;
    }

    @Override
    public String toString() {
        return METHOD;
    }
}
