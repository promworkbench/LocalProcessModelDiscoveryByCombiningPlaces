package org.processmining.placebasedlpmdiscovery.runners.dataattributevectorextraction;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeVectorExtractor;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeVectorExtractorFactory;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dependencyinjection.DataAttributeVectorExtractionDIModule;
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

public class DataAttributeExtractorRunner {
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
        RunnerMetaConfig<DataAttributeVectorExtractionRunnerConfig> metaConfig =
                RunnerMetaConfigReader.dataAttributeVectorExtractionInstance().readConfig(configPath);
        RunnerTimeManager timeManager = new RunnerTimeManager();

        for (DataAttributeVectorExtractionRunnerConfig config : metaConfig.getRunnerConfigs()) {
            System.out.println(config);
            Injector injector = Guice.createInjector(
                    new InputModule(LogUtils
                            .readLogFromFile(config.getInput().get(RunnerInput.EVENT_LOG))),
                    new DataAttributeVectorExtractionDIModule()
            );

            LPMDiscoveryResult result = new FromFileLPMDiscoveryResult(config.getInput().get(RunnerInput.LPMS));
            List<LocalProcessModel> lpms = new ArrayList<>(result.getAllLPMs());

            timeManager.startTimer(config.getOutput().get(RunnerOutput.DATA_ATTRIBUTE_VECTORS));
            DataAttributeVectorExtractorFactory vectorExtractorFactory =
                    injector.getInstance(DataAttributeVectorExtractorFactory.class);
            DataAttributeVectorExtractor vectorExtractor = vectorExtractorFactory.create(config.getAttributes());
            timeManager.stopTimer(config.getOutput().get(RunnerOutput.DATA_ATTRIBUTE_VECTORS));

//            DataAttributeVectorExtractor vectorExtractor = injector.getInstance(DataAttributeVectorExtractor.class);
//            vectorExtractor.chooseSubsetOfAttributes(config.getAttributes());

            writeVectors(config.getOutput().get(RunnerOutput.DATA_ATTRIBUTE_VECTORS),
                    lpms,
                    vectorExtractor.getPositionMapping(),
                    vectorExtractor.convertToVectorsNormalized(lpms));

            writeVectors(config.getOutput().get(RunnerOutput.DATA_ATTRIBUTE_VECTORS).replace(".csv", "-original.csv"),
                    lpms,
                    vectorExtractor.getPositionMapping(),
                    vectorExtractor.convertToVectors(lpms));

        }
        timeManager.exportTimers(metaConfig.getMetaData().get(RunnerMetaConfig.META_DATA_TIMED_EXECUTIONS));
    }

    private static void writeVectors(String filePath,
                                 List<LocalProcessModel> lpms,
                                 List<String> headers,
                                 List<double[]> vectors) throws IOException {
        final int n = lpms.size();

        // Validate sizes
        if (vectors.size() != n) {
            throw new IllegalArgumentException("vectors size (" + vectors.size() + ") != lpms size (" + n + ")");
        }
        for (int i = 0; i < n; i++) {
            double[] row = vectors.get(i);
            if (row == null || row.length != headers.size()) {
                throw new IllegalArgumentException(
                        "vectors[" + i + "] length (" + (row == null ? "null" : row.length) + ") != headers size (" + headers.size() + ")");
            }
        }

        // Stable row order: sort by label, tie-break by original index
        java.util.List<java.util.Map.Entry<String, Integer>> order = java.util.stream.IntStream.range(0, n)
                .mapToObj(i -> new java.util.AbstractMap.SimpleEntry<>(lpms.get(i).getShortString(), i))
                .sorted(java.util.Comparator.<java.util.Map.Entry<String, Integer>, String>comparing(java.util.Map.Entry::getKey)
                        .thenComparingInt(java.util.Map.Entry::getValue))
                .collect(java.util.stream.Collectors.toList());

        // Header: include LPM label column
        java.util.List<String> csvHeader = new java.util.ArrayList<>(headers.size() + 1);
        csvHeader.add("LPM");
        csvHeader.addAll(headers);

        try (org.apache.commons.csv.CSVPrinter csv = org.apache.commons.csv.CSVFormat.DEFAULT
                .builder()
                .setHeader(csvHeader.toArray(new String[0]))
                .build()
                .print(java.nio.file.Paths.get(filePath), java.nio.charset.StandardCharsets.UTF_8)) {

            // Stream rows directly, no intermediate table
            for (java.util.Map.Entry<String, Integer> row : order) {
                int idx = row.getValue();
                double[] vec = vectors.get(idx);

                java.util.List<Object> record = new java.util.ArrayList<>(vec.length + 1);
                record.add(row.getKey()); // LPM label
                for (double v : vec) record.add(v);

                csv.printRecord(record);
            }
        }
    }
}