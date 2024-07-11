package org.processmining.placebasedlpmdiscovery.lpmdistances;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityMeasure;

import java.util.Collection;
import java.util.Map;

public class ModelDistanceUtils {
    public static ModelDistanceConfig createModelDistanceConfig(Map<String, Object> parameters) {
        String type = (String) parameters.get("type");
        Map<String, Object> parameterMap = (Map<String, Object>) parameters.get("parameters");
        switch (type) {
            case "Model Similarity":
                return new ProcessModelSimilarityDistanceConfig(ProcessModelSimilarityMeasure.valueOf(
                        (String) parameterMap.get("modelSimilarity")));
            case "Data Attribute":
                return new DataAttributeModelDistanceConfig((Collection<String>) parameterMap.get("selectedAttributes"));
        }
        throw new NotImplementedException("No implementation for ModelDistanceConfig of type " + type);
    }
}
