package org.processmining.placebasedlpmdiscovery.grouping;

import org.processmining.placebasedlpmdiscovery.model.exporting.gson.GsonSerializable;

import java.util.Map;

public interface ClusteringConfig extends GsonSerializable {
    ClusteringAlgorithm getClusteringAlgorithm();
    Map<String, String> getClusteringParam();
}
