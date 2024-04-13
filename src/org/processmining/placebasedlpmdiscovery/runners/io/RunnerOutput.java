package org.processmining.placebasedlpmdiscovery.runners.io;

public interface RunnerOutput {

    String DISTANCE = "distance";
    String CLUSTERING = "clustering";

    String DATA_ATTRIBUTE_VECTORS = "data_attribute_vectors";

    String get(String key);
}
