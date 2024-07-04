package org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.grouping;

import org.processmining.placebasedlpmdiscovery.grouping.ClusteringAlgorithm;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ClusteringViewConfiguration implements ViewConfiguration {

    private final ClusteringAlgorithm clusteringAlgorithm;
    private final Map<String, String> clusteringParameters;

    public ClusteringViewConfiguration(ClusteringAlgorithm clusteringAlgorithm, Map<String, String> parameters) {
        this.clusteringAlgorithm = clusteringAlgorithm;
        this.clusteringParameters = parameters;
    }

    @Override
    public Map<String, Object> getMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("clusteringAlgorithm", this.clusteringAlgorithm);
        map.put("clusteringParameters", this.clusteringParameters);
        return map;
    }
}
