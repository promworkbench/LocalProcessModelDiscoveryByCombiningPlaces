package org.processmining.placebasedlpmdiscovery.runners.configs;

import java.util.List;
import java.util.Map;

public class RunnerMetaConfig<T extends RunnerConfig> {

    public static final String META_DATA_TIMED_EXECUTIONS = "timedExecutions";

    private final Map<String, String> metaData;
    private final List<T> runnerConfigs;

    public RunnerMetaConfig(Map<String, String> metaData, List<T> runnerConfigs) {
        this.metaData = metaData;
        this.runnerConfigs = runnerConfigs;
    }

    public Map<String, String> getMetaData() {
        return metaData;
    }

    public List<T> getRunnerConfigs() {
        return runnerConfigs;
    }
}
