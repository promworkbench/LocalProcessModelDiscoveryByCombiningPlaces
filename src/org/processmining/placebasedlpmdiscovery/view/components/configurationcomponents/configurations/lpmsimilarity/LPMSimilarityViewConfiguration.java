package org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity;

import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.SimpleMapViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;

import java.util.HashMap;
import java.util.Map;

public class LPMSimilarityViewConfiguration implements ViewConfiguration {

    private final String lpmSimilarityType;
    private final SimpleMapViewConfiguration parameterConfiguration;

    public LPMSimilarityViewConfiguration(String lpmSimilarityType,
                                          SimpleMapViewConfiguration parameterConfiguration) {
        this.lpmSimilarityType = lpmSimilarityType;
        this.parameterConfiguration = parameterConfiguration;
    }

    public String getLpmSimilarityType() {
        return lpmSimilarityType;
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", this.lpmSimilarityType);
        map.put("parameters", this.parameterConfiguration.getParameterMap());
        return map;
    }
}
