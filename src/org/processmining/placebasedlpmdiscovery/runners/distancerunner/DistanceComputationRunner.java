package org.processmining.placebasedlpmdiscovery.runners.distancerunner;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceController;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dependencyinjection.LPMDistancesDependencyInjectionModule;
import org.processmining.placebasedlpmdiscovery.lpmdistances.serialization.ModelDistanceConfigDeserializer;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerInputAdapter;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerOutputDeserializer;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.python.google.common.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DistanceComputationRunner {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("No config file specified.");
            return;
        }

        String configPath = args[0];

        run(configPath);
    }

    private static void run(String configPath) throws Exception {
        // read configs
        List<DistanceRunnerConfig> runnerConfigs = readConfig(configPath);

        for (DistanceRunnerConfig config : runnerConfigs) {
            System.out.println(config);
            Injector injector = Guice.createInjector(
                    new InputModule(LogUtils
                            .readLogFromFile(config.getInput().get(RunnerInput.EVENT_LOG))),
                    new LPMDistancesDependencyInjectionModule()
            );

            System.out.println("Injector initialized");
            LPMDiscoveryResult result = new FromFileLPMDiscoveryResult(config.getInput().get(RunnerInput.LPMS));
            List<LocalProcessModel> lpms = new ArrayList<>(result.getAllLPMs());

            System.out.println("LPMs imported");

            ModelDistanceController modelDistanceController = injector.getInstance(ModelDistanceController.class);
            double[][] distances = modelDistanceController.getDistanceMatrix(lpms, config.getModelDistanceConfig());

            System.out.println("Distances computed");

            writeDistances(config.getOutput().get(RunnerOutput.DISTANCE), lpms, distances);
        }
    }

    private static void writeDistances(String filePath, List<LocalProcessModel> lpms, double[][] distances) throws IOException {
        ImmutableTable.Builder<String, String, Double> tableBuilder = new ImmutableTable.Builder<>();

        for (int i = 0; i < lpms.size(); ++i) {
            for (int j = 0; j < lpms.size(); ++j) {
                tableBuilder.put(lpms.get(i).getShortString(), lpms.get(j).getShortString(), distances[i][j]);
            }
        }

        Table<String, String, Double> distanceTable = tableBuilder.build();
        try (CSVPrinter csvPrinter = CSVFormat.DEFAULT
                .builder()
                .setHeader(lpms.stream().map(LocalProcessModel::getShortString).toArray(String[]::new))
                .build()
                .print(Paths.get(filePath), StandardCharsets.UTF_8)) {
            csvPrinter.printRecords(distanceTable.rowMap().entrySet()
                    .stream().map(entry -> ImmutableList.builder()
                            .add(entry.getKey())
                            .addAll(entry.getValue().values())
                            .build())
                    .collect(Collectors.toList()));
        }
    }

    private static List<DistanceRunnerConfig> readConfig(String configPath) throws FileNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(ModelDistanceConfig.class, new ModelDistanceConfigDeserializer())
                .registerTypeAdapter(RunnerInput.class, new RunnerInputAdapter())
                .registerTypeAdapter(RunnerOutput.class, new RunnerOutputDeserializer());

        Gson gson = gsonBuilder.create();
        List<DistanceRunnerConfig> configs = gson.fromJson(
                new FileReader(configPath),
                new TypeToken<List<DistanceRunnerConfig>>() {
                }.getType()
        );
        return configs;
    }
}
