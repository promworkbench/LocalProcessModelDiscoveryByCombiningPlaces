package org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity;

import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.SimpleMapViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;

import java.util.HashMap;
import java.util.Map;

public class LPMSimilarityViewConfiguration implements ViewConfiguration {

    private final String lpmSimilarityType;
    protected final Map<String, Object> parameterConfiguration;

    public LPMSimilarityViewConfiguration(String lpmSimilarityType,
                                          Map<String, Object> parameterConfiguration) {
        this.lpmSimilarityType = lpmSimilarityType;
        this.parameterConfiguration = parameterConfiguration;
    }

    public LPMSimilarityViewConfiguration(Map<String, Object> map) {
        this.lpmSimilarityType = (String) map.get("type");
        this.parameterConfiguration = (Map<String, Object>) map.get("parameters");
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", this.lpmSimilarityType);
        map.put("parameters", this.parameterConfiguration);
        return map;
    }
}
