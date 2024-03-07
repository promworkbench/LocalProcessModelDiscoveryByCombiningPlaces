package org.processmining.placebasedlpmdiscovery.runners;

public interface RunnerInput {

    String EVENT_LOG = "eventlog";
    String LPMS = "lpms";

    String get(String key);
}
