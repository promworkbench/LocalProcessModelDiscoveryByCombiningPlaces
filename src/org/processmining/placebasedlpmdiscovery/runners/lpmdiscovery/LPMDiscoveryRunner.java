package org.processmining.placebasedlpmdiscovery.runners.lpmdiscovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.serialization.ModelDistanceConfigDeserializer;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.main.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.ExporterFactory;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerInputAdapter;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerOutputDeserializer;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import org.python.google.common.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LPMDiscoveryRunner {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) return;

        String configPath = args[0];

        run(configPath);
    }

    private static void run(String configPath) throws Exception {
        List<LPMDiscoveryRunnerConfig> runnerConfigs = readConfig(configPath);

        for (LPMDiscoveryRunnerConfig config : runnerConfigs) {
            XLog log = LogUtils.readLogFromFile(config.getInput().get("eventlog"));

            PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log));
            parameters.setLpmCount(300);
            parameters.getPlaceChooserParameters().setPlaceLimit(30);

            LPMDiscoveryBuilder builder = Main.createDefaultBuilder(
                    log,
                    new PlaceSet(PlaceUtils.extractPlaceNets(config.getInput().get("places"))),
                    parameters);
            new LPMResult((StandardLPMDiscoveryResult) builder.build().run())
                    .export(ExporterFactory.createLPMDiscoveryResultExporter(),
                            Files.newOutputStream(Paths.get(config.getOutput().get("lpms"))));
        }
    }

    private static List<LPMDiscoveryRunnerConfig> readConfig(String configPath) throws FileNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(ModelDistanceConfig.class, new ModelDistanceConfigDeserializer())
                .registerTypeAdapter(RunnerInput.class, new RunnerInputAdapter())
                .registerTypeAdapter(RunnerOutput.class, new RunnerOutputDeserializer());

        Gson gson = gsonBuilder.create();
        List<LPMDiscoveryRunnerConfig> configs = gson.fromJson(
                new FileReader(configPath),
                new TypeToken<List<LPMDiscoveryRunnerConfig>>() {
                }.getType()
        );
        return configs;
    }

}