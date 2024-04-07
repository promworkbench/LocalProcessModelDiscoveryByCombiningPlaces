package org.processmining.placebasedlpmdiscovery.runners.io;

public interface RunnerOutput {

    String DISTANCE = "distance";
    String CLUSTERING = "clustering";

    String get(String key);
}
