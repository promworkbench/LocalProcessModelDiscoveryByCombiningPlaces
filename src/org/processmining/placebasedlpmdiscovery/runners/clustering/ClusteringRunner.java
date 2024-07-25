package org.processmining.placebasedlpmdiscovery.runners.clustering;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingController;
import org.processmining.placebasedlpmdiscovery.grouping.serialization.GroupingConfigDeserializer;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
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

public class ClusteringRunner {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("No config file specified.");
            return;
        }

        String configPath = args[0];

        run(configPath);
    }

    private static void run(String configPath) throws Exception {
        List<ClusteringRunnerConfig> runnerConfigs = readConfig(configPath);

        for (ClusteringRunnerConfig config : runnerConfigs) {
            LPMDiscoveryResult result = new FromFileLPMDiscoveryResult(config.getInput().get(RunnerInput.LPMS));
            List<LocalProcessModel> lpms = new ArrayList<>(result.getAllLPMs());

            System.out.println("LPMs imported");

            GroupingController groupingController = getGroupingController(
                    config.getClusteringConfig(),
                    LogUtils.readLogFromFile(config.getInput().get(RunnerInput.EVENT_LOG)));
            groupingController.groupLPMs(lpms, config.getClusteringConfig());


            writeClustering(config, lpms);
        }
    }


    public static GroupingController getGroupingController(GroupingConfig config, XLog log) {
        Injector injector = Guice.createInjector(
                new InputModule(log),
                new LPMDistancesDependencyInjectionModule()
        );

        return injector.getInstance(GroupingController.class);
    }

    private static void writeClustering(ClusteringRunnerConfig config, List<LocalProcessModel> lpms) throws IOException {
        ImmutableTable.Builder<String, String, Integer> tableBuilder = new ImmutableTable.Builder<>();

        // store the groups
        for (LocalProcessModel lpm : lpms) {
            String clusterTitle = config.getClusteringConfig().getIdentifier();
            int cluster = lpm.getAdditionalInfo().getGroupsInfo()
                    .getGroupingProperty(clusterTitle);
            tableBuilder.put(lpm.getShortString(), clusterTitle, cluster);
        }

        Table<String, String, Integer> clusteringTable = tableBuilder.build();
        try (CSVPrinter csvPrinter = CSVFormat.DEFAULT
                .builder()
                .setHeader(new String[]{"LPM", config.getClusteringConfig().getIdentifier()})
                .build()
                .print(Paths.get(config.getOutput().get(RunnerOutput.CLUSTERING)), StandardCharsets.UTF_8)) {
            csvPrinter.printRecords(clusteringTable.rowMap().entrySet()
                    .stream().map(entry -> ImmutableList.builder()
                            .add(entry.getKey())
                            .addAll(entry.getValue().values())
                            .build())
                    .collect(Collectors.toList()));
        }
    }

    private static List<ClusteringRunnerConfig> readConfig(String configPath) throws FileNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(GroupingConfig.class, new GroupingConfigDeserializer())
                .registerTypeAdapter(ModelDistanceConfig.class, new ModelDistanceConfigDeserializer())
                .registerTypeAdapter(RunnerInput.class, new RunnerInputAdapter())
                .registerTypeAdapter(RunnerOutput.class, new RunnerOutputDeserializer());

        Gson gson = gsonBuilder.create();
        List<ClusteringRunnerConfig> configs = gson.fromJson(
                new FileReader(configPath),
                new TypeToken<List<ClusteringRunnerConfig>>() {
                }.getType()
        );
        return configs;
    }
}
