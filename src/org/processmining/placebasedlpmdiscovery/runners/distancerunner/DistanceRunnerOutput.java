package org.processmining.placebasedlpmdiscovery.runners.distancerunner;

import org.processmining.placebasedlpmdiscovery.runners.RunnerOutput;

import java.util.Map;

public class DistanceRunnerOutput implements RunnerOutput {
    private final Map<String, String> outputs;

    public DistanceRunnerOutput(Map<String, String> outputs) {
        this.outputs = outputs;
    }
    @Override
    public String get(String key) {
        return outputs.get(key);
    }
}
