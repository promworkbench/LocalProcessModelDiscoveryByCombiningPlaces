package org.processmining.placebasedlpmdiscovery.runners.configs.readers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.processmining.placebasedlpmdiscovery.runners.configs.RunnerMetaConfig;
import org.processmining.placebasedlpmdiscovery.runners.dataattributevectorextraction.DataAttributeVectorExtractionRunnerConfig;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerInputAdapter;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerOutputDeserializer;
import org.python.google.common.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class DataAttributeVectorExtractionRunnerMetaConfigReader implements RunnerMetaConfigReader<DataAttributeVectorExtractionRunnerConfig> {
    @Override
    public RunnerMetaConfig<DataAttributeVectorExtractionRunnerConfig> readConfig(String configFilePath) throws FileNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(RunnerInput.class, new RunnerInputAdapter())
                .registerTypeAdapter(RunnerOutput.class, new RunnerOutputDeserializer());

        Gson gson = gsonBuilder.create();
        RunnerMetaConfig<DataAttributeVectorExtractionRunnerConfig> config = gson.fromJson(
                new FileReader(configFilePath),
                new TypeToken<RunnerMetaConfig<DataAttributeVectorExtractionRunnerConfig>>() {
                }.getType()
        );
        return config;
    }
}
