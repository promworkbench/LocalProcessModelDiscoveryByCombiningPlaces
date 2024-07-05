package org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.grouping;

import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity.LPMSimilarityViewConfiguration;

import java.util.HashMap;
import java.util.Map;

public class GroupingViewConfiguration implements ViewConfiguration {

    private final String identifier;
    private final ClusteringViewConfiguration clusteringViewConfiguration;
    private final LPMSimilarityViewConfiguration lpmSimilarityViewConfiguration;
    public GroupingViewConfiguration(String identifier,
                                     ClusteringViewConfiguration clusteringViewConfiguration,
                                     LPMSimilarityViewConfiguration lpmSimilarityViewConfiguration) {
        this.identifier = identifier;
        this.clusteringViewConfiguration = clusteringViewConfiguration;
        this.lpmSimilarityViewConfiguration = lpmSimilarityViewConfiguration;
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("identifier", this.identifier);
        map.put("clusteringConfig", this.clusteringViewConfiguration.getMap());
        map.put("modelDistanceConfig", this.lpmSimilarityViewConfiguration.getMap());
        return map;
    }
}
