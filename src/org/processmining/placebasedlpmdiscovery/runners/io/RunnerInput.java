package org.processmining.placebasedlpmdiscovery.runners.io;

public interface RunnerInput {

    String EVENT_LOG = "eventlog";
    String LPMS = "lpms";

    String DISTANCE = "distance";

    String get(String key);
}
