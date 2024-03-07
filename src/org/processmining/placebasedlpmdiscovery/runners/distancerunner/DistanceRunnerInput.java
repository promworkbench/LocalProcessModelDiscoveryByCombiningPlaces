package org.processmining.placebasedlpmdiscovery.runners.distancerunner;

import org.processmining.placebasedlpmdiscovery.runners.RunnerInput;

import java.util.Map;

public class DistanceRunnerInput implements RunnerInput {

    private final Map<String, String> inputs;

    public DistanceRunnerInput(Map<String, String> inputs) {
        this.inputs = inputs;
    }
    @Override
    public String get(String key) {
        return inputs.get(key);
    }
}
