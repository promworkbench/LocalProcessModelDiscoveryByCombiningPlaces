package org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

public class PrecomputedFromFileModelDistanceConfig implements ModelDistanceConfig {

    public static final String METHOD = "Precomputed";

    private final String filename;

    @Inject
    public PrecomputedFromFileModelDistanceConfig(String filename) {
        this.filename = filename;
    }

    @Override
    public String getDistanceMethod() {
        return METHOD;
    }

    public String getFilename() {
        return filename;
    }
}
