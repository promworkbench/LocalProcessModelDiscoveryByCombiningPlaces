package org.processmining.placebasedlpmdiscovery.runners.clustering;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingController;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingService;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
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
        RunnerMetaConfig<ClusteringRunnerConfig> metaConfig = RunnerMetaConfigReader.clusteringInstance()
                .readConfig(configPath);
        RunnerTimeManager timeManager = new RunnerTimeManager();

        for (ClusteringRunnerConfig config : metaConfig.getRunnerConfigs()) {
            LPMDiscoveryResult result = new FromFileLPMDiscoveryResult(config.getInput().get(RunnerInput.LPMS));
            List<LocalProcessModel> lpms = new ArrayList<>(result.getAllLPMs());

            System.out.println("LPMs imported");

            XLog eventLog = LogUtils.readLogFromFile(config.getInput().get(RunnerInput.EVENT_LOG));

            timeManager.startTimer(config.getOutput().get(RunnerOutput.CLUSTERING));
            GroupingService groupingService = GroupingService.getInstance(eventLog);
            groupingService.groupLPMs(lpms, config.getClusteringConfig());
            timeManager.stopTimer(config.getOutput().get(RunnerOutput.CLUSTERING));

//            GroupingController groupingController = getGroupingController(config.getClusteringConfig(), eventLog);
//            groupingController.groupLPMs(lpms, config.getClusteringConfig());


            writeClustering(config, lpms);
        }
        timeManager.exportTimers(metaConfig.getMetaData().get(RunnerMetaConfig.META_DATA_OUTPUT_DIR) + "/times.csv");
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
}