package org.processmining.placebasedlpmdiscovery.grouping;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;

import java.util.Map;

public interface GroupingConfig {

    ClusteringAlgorithm getClusteringAlgorithm();

    Map<String, String> getClusteringConfig();

    ModelDistanceConfig getModelDistanceConfig();

    String getIdentifier();
}