package org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations;

import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity.LPMSimilarityViewConfiguration;

import java.util.Map;
import java.util.stream.Collectors;

public class SimpleMapViewConfiguration implements ViewConfiguration {

    private final Map<String, String> parameterMap;

    public SimpleMapViewConfiguration(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public Map<String, Object> getMap() {
        return parameterMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
