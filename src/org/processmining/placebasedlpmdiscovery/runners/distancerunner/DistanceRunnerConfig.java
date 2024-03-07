package org.processmining.placebasedlpmdiscovery.runners.distancerunner;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.runners.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.RunnerOutput;
import org.processmining.placebasedlpmdiscovery.runners.AbstractRunnerConfig;

public class DistanceRunnerConfig extends AbstractRunnerConfig {

    private final ModelDistanceConfig modelDistanceConfig;

    public DistanceRunnerConfig(ModelDistanceConfig modelDistanceConfig,
                                DistanceRunnerInput runnerInput,
                                DistanceRunnerOutput runnerOutput) {
        super(runnerInput, runnerOutput);
        this.modelDistanceConfig = modelDistanceConfig;
    }

    public ModelDistanceConfig getModelDistanceConfig() {
        return this.modelDistanceConfig;
    }

}
