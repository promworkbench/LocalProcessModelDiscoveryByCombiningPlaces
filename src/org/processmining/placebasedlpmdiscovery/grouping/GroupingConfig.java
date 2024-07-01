package org.processmining.placebasedlpmdiscovery.grouping;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

import java.util.Map;

public interface GroupingConfig {
    ClusteringConfig getClusteringConfig();

    ModelDistanceConfig getModelDistanceConfig();

    String getIdentifier();

    void setIdentifier(String id);
}
