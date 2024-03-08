package org.processmining.placebasedlpmdiscovery.runners.distancerunner;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.runners.AbstractRunnerConfig;
import org.processmining.placebasedlpmdiscovery.runners.io.DefaultRunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.DefaultRunnerOutput;

public class DistanceRunnerConfig extends AbstractRunnerConfig {

    private final ModelDistanceConfig modelDistanceConfig;

    public DistanceRunnerConfig(ModelDistanceConfig modelDistanceConfig,
                                DefaultRunnerInput runnerInput,
                                DefaultRunnerOutput runnerOutput) {
        super(runnerInput, runnerOutput);
        this.modelDistanceConfig = modelDistanceConfig;
    }

    public ModelDistanceConfig getModelDistanceConfig() {
        return this.modelDistanceConfig;
    }

}
