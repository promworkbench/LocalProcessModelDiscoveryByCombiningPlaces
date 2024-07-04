package org.processmining.placebasedlpmdiscovery.grouping;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceUtils;

import java.util.Map;

public class GroupingUtils {

    public static GroupingConfig createGroupingConfig(Map<String, Object> parameters) {
        return new DefaultGroupingConfig((String) parameters.get("identifier"),
                GroupingUtils.createClusteringConfig(
                        (Map<String, Object>) parameters.get("clusteringConfig")),
                ModelDistanceUtils.createModelDistanceConfig(
                        (Map<String, Object>) parameters.get("modelDistanceConfig")));
    }

    private static ClusteringConfig createClusteringConfig(Map<String, Object> parameters) {
        return new DefaultClusteringConfig(
                (ClusteringAlgorithm) parameters.get("clusteringAlgorithm"),
                (Map<String, String>) parameters.get("clusteringParameters"));
    }
}
