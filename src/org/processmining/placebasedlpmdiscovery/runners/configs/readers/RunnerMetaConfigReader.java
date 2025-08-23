package org.processmining.placebasedlpmdiscovery.runners.configs.readers;

import org.processmining.placebasedlpmdiscovery.runners.configs.RunnerConfig;
import org.processmining.placebasedlpmdiscovery.runners.configs.RunnerMetaConfig;

import java.io.FileNotFoundException;

public interface RunnerMetaConfigReader<T extends RunnerConfig> {

    static LPMDiscoveryRunnerMetaConfigReader lpmDiscoveryInstance() {
        return new LPMDiscoveryRunnerMetaConfigReader();
    }

    RunnerMetaConfig<T> readConfig(String configFilePath) throws FileNotFoundException;
}
