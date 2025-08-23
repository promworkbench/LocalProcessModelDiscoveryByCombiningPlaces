package org.processmining.placebasedlpmdiscovery.runners.configs;

import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;

public interface RunnerConfig {

    RunnerOutput getOutput();

    RunnerInput getInput();
}
