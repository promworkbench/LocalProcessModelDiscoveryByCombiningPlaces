package org.processmining.placebasedlpmdiscovery.runners.io;

import java.util.Map;

public class DefaultRunnerInput implements RunnerInput {

    private final Map<String, String> inputs;

    public DefaultRunnerInput(Map<String, String> inputs) {
        this.inputs = inputs;
    }
    @Override
    public String get(String key) {
        return inputs.get(key);
    }
}
