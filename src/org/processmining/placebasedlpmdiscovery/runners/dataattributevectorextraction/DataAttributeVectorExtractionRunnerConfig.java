package org.processmining.placebasedlpmdiscovery.runners.dataattributevectorextraction;

import org.processmining.placebasedlpmdiscovery.runners.AbstractRunnerConfig;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;

import java.util.Collection;

public class DataAttributeVectorExtractionRunnerConfig extends AbstractRunnerConfig {

    private final Collection<String> attributes;

    public DataAttributeVectorExtractionRunnerConfig(Collection<String> attributes,
                                                     RunnerInput runnerInput, RunnerOutput runnerOutput) {
        super(runnerInput, runnerOutput);
        this.attributes = attributes;
    }

    public Collection<String> getAttributes() {
        return attributes;
    }
}
