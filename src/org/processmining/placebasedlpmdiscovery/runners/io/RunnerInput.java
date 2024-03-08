package org.processmining.placebasedlpmdiscovery.runners.io;

public interface RunnerInput {

    String EVENT_LOG = "eventlog";
    String LPMS = "lpms";

    String get(String key);
}
