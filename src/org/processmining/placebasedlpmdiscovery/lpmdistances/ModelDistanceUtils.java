package org.processmining.placebasedlpmdiscovery.lpmdistances;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.MixedModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.mixed.WeightedModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityMeasure;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity.LPMSimilarityViewConfiguration;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class ModelDistanceUtils {
    public static ModelDistanceConfig createModelDistanceConfig(Map<String, Object> configMap) {
        String type = (String) configMap.get("type");
        Map<String, Object> parameterMap = (Map<String, Object>) configMap.get("parameters");
        switch (type) {
            case "Model Similarity":
                return new ProcessModelSimilarityDistanceConfig(
                        (ProcessModelSimilarityMeasure) parameterMap.get("modelSimilarity"));
            case "Data Attribute":
                return new DataAttributeModelDistanceConfig((Collection<String>) parameterMap.get("selectedAttributes"));
            case "Mixed":
                Map<String, Double> weights = (Map<String, Double>) parameterMap.get("weights");
                Map<String, Map<String, Object>> configs = (Map<String, Map<String, Object>>) parameterMap.get("configs");
                return new MixedModelDistanceConfig(weights.keySet().stream().map(name ->
                        new WeightedModelDistanceConfig(name,
                                ModelDistanceUtils.createModelDistanceConfig(configs.get(name)),
                                weights.get(name)))
                        .collect(Collectors.toList()));
        }
        throw new NotImplementedException("No implementation for ModelDistanceConfig of type " + type);
    }
}
