package org.processmining.placebasedlpmdiscovery.lpmdistances;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityMeasure;

import java.util.Map;

public class ModelDistanceUtils {
    public static ModelDistanceConfig createModelDistanceConfig(Map<String, Object> parameters) {
        String type = (String) parameters.get("type");
        Map<String, String> parameterMap = (Map<String, String>) parameters.get("parameters");
        switch (type) {
            case "Model Similarity":
                return new ProcessModelSimilarityDistanceConfig(ProcessModelSimilarityMeasure.valueOf(parameterMap.get(
                        "modelSimilarity")));
        }
        throw new NotImplementedException("No implementation for ModelDistanceConfig of type " + type);
    }
}
