package org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.grouping;

import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;

import java.util.HashMap;
import java.util.Map;

public class GroupingViewConfiguration implements ViewConfiguration {

    private final String identifier;
    private final ClusteringViewConfiguration clusteringViewConfiguration;
    public GroupingViewConfiguration(String identifier, ClusteringViewConfiguration clusteringViewConfiguration) {
        this.identifier = identifier;
        this.clusteringViewConfiguration = clusteringViewConfiguration;
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("identifier", this.identifier);
        map.put("clusteringConfig", this.clusteringViewConfiguration.getMap());
        map.put("modelDistanceConfig", new HashMap<>());
        return map;
    }
}
