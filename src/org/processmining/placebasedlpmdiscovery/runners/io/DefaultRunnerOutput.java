package org.processmining.placebasedlpmdiscovery.runners.io;

import java.util.Map;

public class DefaultRunnerOutput implements RunnerOutput {
    private final Map<String, String> outputs;

    public DefaultRunnerOutput(Map<String, String> outputs) {
        this.outputs = outputs;
    }
    @Override
    public String get(String key) {
        return outputs.get(key);
    }
}
