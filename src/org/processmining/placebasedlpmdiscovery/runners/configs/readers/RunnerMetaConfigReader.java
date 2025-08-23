package org.processmining.placebasedlpmdiscovery.runners.configs.readers;

import org.processmining.placebasedlpmdiscovery.runners.configs.RunnerConfig;
import org.processmining.placebasedlpmdiscovery.runners.configs.RunnerMetaConfig;

import java.io.FileNotFoundException;

public interface RunnerMetaConfigReader<T extends RunnerConfig> {

    static LPMDiscoveryRunnerMetaConfigReader lpmDiscoveryInstance() {
        return new LPMDiscoveryRunnerMetaConfigReader();
    }

    static ClusteringRunnerMetaConfigReader clusteringInstance() {
        return new ClusteringRunnerMetaConfigReader();
    }

    static DataAttributeVectorExtractionRunnerMetaConfigReader dataAttributeVectorExtractionInstance() {
        return new DataAttributeVectorExtractionRunnerMetaConfigReader();
    }

    static DistanceRunnerMetaConfigReader distanceInstance() {
        return new DistanceRunnerMetaConfigReader();
    }

    RunnerMetaConfig<T> readConfig(String configFilePath) throws FileNotFoundException;
}
