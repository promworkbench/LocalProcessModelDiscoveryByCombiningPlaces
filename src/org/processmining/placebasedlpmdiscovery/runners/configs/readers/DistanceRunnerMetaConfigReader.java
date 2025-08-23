package org.processmining.placebasedlpmdiscovery.runners.configs.readers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.serialization.ModelDistanceConfigDeserializer;
import org.processmining.placebasedlpmdiscovery.runners.configs.RunnerMetaConfig;
import org.processmining.placebasedlpmdiscovery.runners.distancerunner.DistanceRunnerConfig;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerInputAdapter;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerOutputDeserializer;
import org.python.google.common.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class DistanceRunnerMetaConfigReader implements RunnerMetaConfigReader<DistanceRunnerConfig> {
    @Override
    public RunnerMetaConfig<DistanceRunnerConfig> readConfig(String configFilePath) throws FileNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(ModelDistanceConfig.class, new ModelDistanceConfigDeserializer())
                .registerTypeAdapter(RunnerInput.class, new RunnerInputAdapter())
                .registerTypeAdapter(RunnerOutput.class, new RunnerOutputDeserializer());

        Gson gson = gsonBuilder.create();
        RunnerMetaConfig<DistanceRunnerConfig> config = gson.fromJson(
                new FileReader(configFilePath),
                new TypeToken<List<DistanceRunnerConfig>>() {
                }.getType()
        );
        return config;
    }
}
