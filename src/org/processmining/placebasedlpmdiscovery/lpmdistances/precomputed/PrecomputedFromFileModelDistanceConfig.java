package org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

public class PrecomputedFromFileModelDistanceConfig implements ModelDistanceConfig {

    public static final String METHOD = "Precomputed";

    private final String filename;
    private final ModelDistanceConfig originalConfig;

    public PrecomputedFromFileModelDistanceConfig(String filename, ModelDistanceConfig originalConfig) {
        this.filename = filename;
        this.originalConfig = originalConfig;
    }

    @Override
    public String getDistanceMethod() {
        return METHOD;
    }

    public String getFilename() {
        return filename;
    }
}
