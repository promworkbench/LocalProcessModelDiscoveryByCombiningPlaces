package org.processmining.placebasedlpmdiscovery.runners.dataattributevectorextraction;

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
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeVectorExtractor;
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
        List<DataAttributeVectorExtractionRunnerConfig> runnerConfigs = readConfig(configPath);

        for (DataAttributeVectorExtractionRunnerConfig config : runnerConfigs) {
            System.out.println(config);
            Injector injector = Guice.createInjector(
                    new InputModule(LogUtils
                            .readLogFromFile(config.getInput().get(RunnerInput.EVENT_LOG)))
//                    new DataAttributeVectorExtractionDIModule()
            );

            LPMDiscoveryResult result = new FromFileLPMDiscoveryResult(config.getInput().get(RunnerInput.LPMS));
            List<LocalProcessModel> lpms = new ArrayList<>(result.getAllLPMs());

            DataAttributeVectorExtractor vectorExtractor = injector.getInstance(DataAttributeVectorExtractor.class);
            vectorExtractor.chooseSubsetOfAttributes(config.getAttributes());

            writeVectors(config.getOutput().get(RunnerOutput.DATA_ATTRIBUTE_VECTORS),
                    lpms,
                    vectorExtractor.getPositionMapping(),
                    vectorExtractor.convertToVectors(lpms));

        }
    }

    private static List<DataAttributeVectorExtractionRunnerConfig> readConfig(String configPath) throws FileNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(RunnerInput.class, new RunnerInputAdapter())
                .registerTypeAdapter(RunnerOutput.class, new RunnerOutputDeserializer());

        Gson gson = gsonBuilder.create();
        List<DataAttributeVectorExtractionRunnerConfig> configs = gson.fromJson(
                new FileReader(configPath),
                new TypeToken<List<DataAttributeVectorExtractionRunnerConfig>>() {
                }.getType()
        );
        return configs;
    }

    private static void writeVectors(String filePath,
                                     List<LocalProcessModel> lpms,
                                     List<String> headers,
                                     List<double[]> distances) throws IOException {
        ImmutableTable.Builder<String, String, Double> tableBuilder = new ImmutableTable.Builder<>();

        for (int i = 0; i < lpms.size(); ++i) {
            for (int j = 0; j < headers.size(); ++j) {
                tableBuilder.put(lpms.get(i).getShortString(), headers.get(j), distances.get(i)[j]);
            }
        }

        Table<String, String, Double> distanceTable = tableBuilder.build();
        try (CSVPrinter csvPrinter = CSVFormat.DEFAULT
                .builder()
                .setHeader(headers.toArray(new String[0]))
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
}
