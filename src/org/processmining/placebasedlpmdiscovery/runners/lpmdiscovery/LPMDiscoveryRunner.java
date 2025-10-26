package org.processmining.placebasedlpmdiscovery.runners.lpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.LPMDiscovery;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.CSVExporter;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.ExporterFactory;
import org.processmining.placebasedlpmdiscovery.runners.configs.RunnerMetaConfig;
import org.processmining.placebasedlpmdiscovery.runners.configs.readers.RunnerMetaConfigReader;
import org.processmining.placebasedlpmdiscovery.runners.timemanagement.RunnerTimeManager;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

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
        RunnerMetaConfig<LPMDiscoveryRunnerConfig> metaConfig = RunnerMetaConfigReader
                .lpmDiscoveryInstance().readConfig(configPath);
        RunnerTimeManager timeManager = new RunnerTimeManager();

        for (LPMDiscoveryRunnerConfig config : metaConfig.getRunnerConfigs()) {
            System.out.println(config.getInput().get("eventlog"));
            XLog log = LogUtils.readLogFromFile(config.getInput().get("eventlog"));

            timeManager.startTimer(config.getOutput().get("lpms"));
            LPMDiscoveryResult result = LPMDiscovery.getInstance().from(log);
            timeManager.stopTimer(config.getOutput().get("lpms"));

            System.out.println(result.getAllLPMs().size());
            CSVExporter.export(result, config.getOutput().get("lpms"));
            result.export(ExporterFactory.createLPMDiscoveryResultExporter(),
                    Files.newOutputStream(Paths.get(config.getOutput().get("lpms")+".json")));

//            File coverageResFile = new File(config.getOutput().get("lpms").replaceFirst(".json", "-coverage.csv"));
//            ((LPMEvaluationController.EventCoverageSetLevel) result.getAdditionalResults().get("eventCoverageSetLevel"))
//                    .export(coverageResFile);
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
        timeManager.exportTimers(metaConfig.getMetaData().get(RunnerMetaConfig.META_DATA_TIMED_EXECUTIONS));
    }

}
