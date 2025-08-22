package org.processmining.placebasedlpmdiscovery.runners.clustering;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.grouping.ClusteringConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingController;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingService;
import org.processmining.placebasedlpmdiscovery.grouping.serialization.GroupingConfigDeserializer;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dependencyinjection.LPMDistancesDependencyInjectionModule;
import org.processmining.placebasedlpmdiscovery.lpmdistances.serialization.ModelDistanceConfigDeserializer;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.exporting.gson.adapters.GeneralInterfaceAdapter;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerInput;
import org.processmining.placebasedlpmdiscovery.runners.io.RunnerOutput;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerInputAdapter;
import org.processmining.placebasedlpmdiscovery.runners.serialization.RunnerOutputDeserializer;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.python.google.common.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LPMClusteringRunner {

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

            XLog eventLog = LogUtils.readLogFromFile(config.getInput().get(RunnerInput.EVENT_LOG));
            GroupingService groupingService = GroupingService.getInstance(eventLog);
            groupingService.groupLPMs(lpms, config.getClusteringConfig());
//            GroupingController groupingController = getGroupingController(config.getClusteringConfig(), eventLog);
//            groupingController.groupLPMs(lpms, config.getClusteringConfig());


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
        final String clusterTitle = config.getClusteringConfig().getIdentifier();

        // Deterministic row order: sort by LPM short string, tie-break by original index
        java.util.List<Integer> order = java.util.stream.IntStream.range(0, lpms.size())
                .boxed()
                .sorted(java.util.Comparator
                        .comparing((java.util.function.Function<Integer, String>) i -> lpms.get(i).getShortString())
                        .thenComparingInt(i -> i))
                .collect(java.util.stream.Collectors.toList());

        try (org.apache.commons.csv.CSVPrinter csv = org.apache.commons.csv.CSVFormat.DEFAULT
                .builder()
                .setHeader("LPM", clusterTitle)
                .build()
                .print(java.nio.file.Paths.get(config.getOutput().get(RunnerOutput.CLUSTERING)),
                        java.nio.charset.StandardCharsets.UTF_8)) {

            for (Integer i : order) {
                LocalProcessModel lpm = lpms.get(i);
                int cluster = lpm.getAdditionalInfo()
                        .getGroupsInfo()
                        .getGroupingProperty(clusterTitle);
                csv.printRecord(lpm.getShortString(), cluster);
            }
        }
    }

    private static List<ClusteringRunnerConfig> readConfig(String configPath) throws FileNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .registerTypeAdapter(GroupingConfig.class, new GroupingConfigDeserializer())
                .registerTypeAdapter(ModelDistanceConfig.class, new ModelDistanceConfigDeserializer())
                .registerTypeAdapter(RunnerInput.class, new RunnerInputAdapter())
                .registerTypeAdapter(RunnerOutput.class, new RunnerOutputDeserializer())
                .registerTypeAdapter(ClusteringConfig.class, new GeneralInterfaceAdapter<ClusteringConfig>());

        Gson gson = gsonBuilder.create();
        List<ClusteringRunnerConfig> configs = gson.fromJson(
                new FileReader(configPath),
                new TypeToken<List<ClusteringRunnerConfig>>() {
                }.getType()
        );
        return configs;
    }
}