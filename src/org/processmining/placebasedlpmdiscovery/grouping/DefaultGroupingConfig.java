package org.processmining.placebasedlpmdiscovery.grouping;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;

import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DefaultGroupingConfig implements GroupingConfig {

    private String identifier;
    private final ClusteringConfig clusteringConfig;
    private final ModelDistanceConfig modelDistanceConfig;

    public DefaultGroupingConfig() {
        this("default",
                new DefaultClusteringConfig(),
                new DataAttributeModelDistanceConfig());
    }

    public DefaultGroupingConfig(String identifier,
                                 ClusteringConfig clusteringConfig,
                                 ModelDistanceConfig modelDistanceConfig) {
        this.identifier = identifier;
        this.clusteringConfig = clusteringConfig;
        this.modelDistanceConfig = modelDistanceConfig;
    }

    @Override
    public ClusteringConfig getClusteringConfig() { return this.clusteringConfig; }

    @Override
    public ModelDistanceConfig getModelDistanceConfig() {
        return this.modelDistanceConfig;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setIdentifier(String id) {
        this.identifier = id;
    }
}
