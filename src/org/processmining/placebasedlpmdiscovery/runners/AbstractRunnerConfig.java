package org.processmining.placebasedlpmdiscovery.runners;

import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;

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
