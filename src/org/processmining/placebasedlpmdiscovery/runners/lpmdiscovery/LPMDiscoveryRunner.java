package org.processmining.placebasedlpmdiscovery.runners.lpmdiscovery;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.LPMDiscovery;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.serialization.ModelDistanceConfigDeserializer;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.ExporterFactory;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerInputAdapter;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerOutputDeserializer;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.python.google.common.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class LPMDiscoveryRunner {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("No xes file provided.");
            return;
        }

        String configPath = args[0];

        run(configPath);
    }

    private static void run(String configPath) throws Exception {
        List<LPMDiscoveryRunnerConfig> runnerConfigs = readConfig(configPath);

        for (LPMDiscoveryRunnerConfig config : runnerConfigs) {
            System.out.println(config.getInput().get("eventlog"));
            XLog log = LogUtils.readLogFromFile(config.getInput().get("eventlog"));
            LPMDiscoveryResult result = LPMDiscovery.getInstance().from(log);
            System.out.println(result.getAllLPMs().size());
            result.export(ExporterFactory.createLPMDiscoveryResultExporter(),
                    Files.newOutputStream(Paths.get(config.getOutput().get("lpms"))));
            File coverageResFile = new File(config.getOutput().get("lpms").replaceFirst(".json", "-coverage.csv"));
            ((LPMEvaluationController.EventCoverageSetLevel) result.getAdditionalResults().get("eventCoverageSetLevel"))
                    .export(coverageResFile);
//            EventLog eventLog = new XLogWrapper(log);
//            PlaceSet placeSet = new PlaceSet(PlaceUtils.extractPlaceNets(config.getInput().get("places")));
//
//            PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log));
//            parameters.setLpmCount(300);
//            parameters.getPlaceChooserParameters().setPlaceLimit(30);
//
//            LPMDiscoveryAlgBuilder builder = Main.createDefaultBuilder(log, parameters);
//            new LPMResult((StandardLPMDiscoveryResult) builder.build().run(new StandardLPMDiscoveryInput(eventLog,
//                    new FPGrowthForPlacesLPMBuildingInput(eventLog, placeSet.getPlaces().getPlaces())),
//                    parameters))
//                    .export(ExporterFactory.createLPMDiscoveryResultExporter(),
//                            Files.newOutputStream(Paths.get(config.getOutput().get("lpms"))));
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
