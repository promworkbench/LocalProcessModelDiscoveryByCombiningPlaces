package org.processmining.placebasedlpmdiscovery.grouping;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultClusteringConfig implements ClusteringConfig {
    private final ClusteringAlgorithm clusteringAlgorithm;
    private final Map<String, String> clusteringParam;

    public DefaultClusteringConfig() {
        this(ClusteringAlgorithm.Hierarchical,
                Stream.of(new AbstractMap.SimpleEntry<>("num_clusters", "10"),
                                new AbstractMap.SimpleEntry<>("linkage", "complete"))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }
    public DefaultClusteringConfig(ClusteringAlgorithm clusteringAlgorithm,
                                   Map<String, String> clusteringParam) {
        this.clusteringAlgorithm = clusteringAlgorithm;
        this.clusteringParam = clusteringParam;
    }

    @Override
    public ClusteringAlgorithm getClusteringAlgorithm() {
        return this.clusteringAlgorithm;
    }

    @Override
    public Map<String, String> getClusteringParam() {
        return this.clusteringParam;
    }
}
