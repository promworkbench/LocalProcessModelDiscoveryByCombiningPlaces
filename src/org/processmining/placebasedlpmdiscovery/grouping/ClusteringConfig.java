package org.processmining.placebasedlpmdiscovery.grouping;

import java.util.Map;

public interface ClusteringConfig {
    ClusteringAlgorithm getClusteringAlgorithm();
    Map<String, String> getClusteringParam();
}
