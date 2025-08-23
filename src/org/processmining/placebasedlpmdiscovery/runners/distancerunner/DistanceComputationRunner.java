package org.processmining.placebasedlpmdiscovery.runners.distancerunner;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceService;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dependencyinjection.LPMDistancesDependencyInjectionModule;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.runners.configs.RunnerMetaConfig;
import org.processmining.placebasedlpmdiscovery.runners.configs.readers.RunnerMetaConfigReader;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;
import org.processmining.placebasedlpmdiscovery.runners.timemanagement.RunnerTimeManager;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        RunnerMetaConfig<DistanceRunnerConfig> metaConfig = RunnerMetaConfigReader.distanceInstance().readConfig(configPath);
        RunnerTimeManager timeManager = new RunnerTimeManager();

        for (DistanceRunnerConfig config : metaConfig.getRunnerConfigs()) {
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

            timeManager.startTimer(config.getOutput().get(RunnerOutput.DISTANCE));
            ModelDistanceService modelDistanceService = injector.getInstance(ModelDistanceService.class);
            double[][] distances = modelDistanceService.getDistanceMatrix(lpms, config.getModelDistanceConfig());
            timeManager.stopTimer(config.getOutput().get(RunnerOutput.DISTANCE));

            System.out.println("Distances computed");

            writeDistances(config.getOutput().get(RunnerOutput.DISTANCE), lpms, distances);
        }
        timeManager.exportTimers(metaConfig.getMetaData().get(RunnerMetaConfig.META_DATA_TIMED_EXECUTIONS));
    }

    private static void writeDistances(String filePath, List<LocalProcessModel> lpms, double[][] distances) throws IOException {
        final int n = lpms.size();

        // Validate matrix shape
        if (distances.length != n) {
            throw new IllegalArgumentException("distances row count (" + distances.length + ") != lpms size (" + n + ")");
        }
        for (int i = 0; i < n; i++) {
            if (distances[i].length != n) {
                throw new IllegalArgumentException("distances[" + i + "] length (" + distances[i].length + ") != lpms size (" + n + ")");
            }
        }

        // Build a stable, sorted order of (label, index); tie-break by index to handle duplicate labels
        java.util.List<java.util.Map.Entry<String, Integer>> order = java.util.stream.IntStream.range(0, n)
                .mapToObj(i -> new java.util.AbstractMap.SimpleEntry<>(lpms.get(i).getShortString(), i))
                .sorted(java.util.Comparator.<java.util.Map.Entry<String, Integer>, String>comparing(java.util.Map.Entry::getKey)
                        .thenComparingInt(java.util.Map.Entry::getValue))
                .collect(java.util.stream.Collectors.toList());

        // Header: "LPM" + sorted labels (duplicates allowed; order is stable)
        java.util.List<String> header = new java.util.ArrayList<>(n + 1);
        header.add("LPM");
        order.stream().map(java.util.Map.Entry::getKey).forEach(header::add);

        try (org.apache.commons.csv.CSVPrinter csv = org.apache.commons.csv.CSVFormat.DEFAULT
                .builder()
                .setHeader(header.toArray(new String[0]))
                .build()
                .print(java.nio.file.Paths.get(filePath), java.nio.charset.StandardCharsets.UTF_8)) {

            // Write each row in the same order
            for (java.util.Map.Entry<String, Integer> row : order) {
                java.util.List<Object> record = new java.util.ArrayList<>(n + 1);
                record.add(row.getKey());
                int ri = row.getValue();
                for (java.util.Map.Entry<String, Integer> col : order) {
                    record.add(distances[ri][col.getValue()]);
                }
                csv.printRecord(record);
            }
        }
    }
}