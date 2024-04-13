package org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

import java.util.Collection;

public class DataAttributeModelDistanceConfig implements ModelDistanceConfig {

    public static final String METHOD = "DataAttribute";

    private Collection<String> attributes;

    public Collection<String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Collection<String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getDistanceMethod() {
        return METHOD;
    }

    @Override
    public String toString() {
        return METHOD;
    }
}
