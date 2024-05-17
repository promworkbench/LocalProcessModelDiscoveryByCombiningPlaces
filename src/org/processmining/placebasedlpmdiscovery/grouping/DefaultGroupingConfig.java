package org.processmining.placebasedlpmdiscovery.grouping;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultGroupingConfig implements GroupingConfig {

    private final String identifier;
    private final ClusteringAlgorithm clusteringAlgorithm;
    private final Map<String, String> clusteringConfig;
    private final ModelDistanceConfig modelDistanceConfig;

    public DefaultGroupingConfig() {
        this("default",
                ClusteringAlgorithm.Hierarchical,
                Stream.of(new AbstractMap.SimpleEntry<>("num_clusters", "10"),
                                new AbstractMap.SimpleEntry<>("linkage", "complete"))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)),
                new DataAttributeModelDistanceConfig()
        );
    }

    public DefaultGroupingConfig(String identifier,
                                 ClusteringAlgorithm clusteringAlgorithm,
                                 Map<String, String> clusteringConfig,
                                 ModelDistanceConfig modelDistanceConfig) {
        this.identifier = identifier;
        this.clusteringAlgorithm = clusteringAlgorithm;
        this.clusteringConfig = clusteringConfig;
        this.modelDistanceConfig = modelDistanceConfig;
    }

    @Override
    public ClusteringAlgorithm getClusteringAlgorithm() {
        return this.clusteringAlgorithm;
    }

    @Override
    public Map<String, String> getClusteringConfig() {
        return this.clusteringConfig;
    }

    @Override
    public ModelDistanceConfig getModelDistanceConfig() {
        return this.modelDistanceConfig;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }
}