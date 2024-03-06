package org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

public class ProcessModelSimilarityDistanceConfig implements ModelDistanceConfig {

    public static final String METHOD = "ProcessModelSimilarity";

    private final ProcessModelSimilarityMeasure processModelSimilarityMeasure;

    public ProcessModelSimilarityDistanceConfig(ProcessModelSimilarityMeasure processModelSimilarityMeasure) {
        this.processModelSimilarityMeasure = processModelSimilarityMeasure;
    }

    @Override
    public String getDistanceMethod() {
        return ProcessModelSimilarityDistanceConfig.METHOD;
    }

    public ProcessModelSimilarityMeasure getProcessModelSimilarityMeasure() {
        return this.processModelSimilarityMeasure;
    }
}
