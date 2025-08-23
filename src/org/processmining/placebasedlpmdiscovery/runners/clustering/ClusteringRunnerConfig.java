package org.processmining.placebasedlpmdiscovery.runners.clustering;

import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;
import org.processmining.placebasedlpmdiscovery.runners.configs.AbstractRunnerConfig;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;

public class ClusteringRunnerConfig extends AbstractRunnerConfig {

    private final GroupingConfig clusteringConfig;

    public ClusteringRunnerConfig(GroupingConfig clusteringConfig,
                                  RunnerInput runnerInput,
                                  RunnerOutput runnerOutput) {
        super(runnerInput, runnerOutput);
        this.clusteringConfig = clusteringConfig;
    }

    public GroupingConfig getClusteringConfig() {
        return clusteringConfig;
    }

    @Override
    public String toString() {
        return "ClusteringRunnerConfig{" +
                "clusteringConfig=" + clusteringConfig +
                ", runnerInput=" + runnerInput +
                ", runnerOutput=" + runnerOutput +
                '}';
    }
}
