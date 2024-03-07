package org.processmining.placebasedlpmdiscovery.runners;

import org.processmining.placebasedlpmdiscovery.runners.RunnerConfig;
import org.processmining.placebasedlpmdiscovery.runners.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.RunnerOutput;

public abstract class AbstractRunnerConfig implements RunnerConfig {

    protected final RunnerInput runnerInput;
    protected final RunnerOutput runnerOutput;

    public AbstractRunnerConfig(RunnerInput runnerInput, RunnerOutput runnerOutput) {
        this.runnerInput = runnerInput;
        this.runnerOutput = runnerOutput;
    }

    @Override
    public RunnerOutput getOutput() {
        return this.runnerOutput;
    }

    @Override
    public RunnerInput getInput() {
        return this.runnerInput;
    }
}
