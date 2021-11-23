package org.processmining.placebasedlpmdiscovery.utils.analysis;

import org.apache.logging.log4j.util.Strings;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.cli.CLIContext;
import org.processmining.contexts.cli.CLIPluginContext;
import org.processmining.placebasedlpmdiscovery.loganalyzer.LogAnalyzer;
import org.processmining.placebasedlpmdiscovery.loganalyzer.LogAnalyzerParameters;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryAlgorithmId;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.PlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryPlugin;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Runner {

    public static void main(String[] args) {
        try {
            Scanner scn = new Scanner(System.in);

            // event logs folder
            System.out.println("Enter the absolute path to the folder where the event logs are stored:");
            String eventLogsAndPlacesPath = scn.nextLine();

            // parameter setup file
            System.out.println("Enter the absolute path to the file where the parameter setup is given:");
            String parameterPath = scn.nextLine();

            // folder where the statistics should be outputted
            System.out.println("Enter the absolute path to the folder where the statistics should be outputted:");
            String statisticsPath = scn.nextLine();

            // create the event log -- set of places map
            List<Map.Entry<String, String>> logMap = getLogMap(eventLogsAndPlacesPath);

            // run the algorithm for all event logs
            runAlgorithmForDifferentSettingsAndEventLogs(logMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Path> getAllXesFilesInFolder(String folderName) {
        try {
            return Files
                    .walk(Paths.get(folderName))
                    .filter(path -> String.valueOf(path).endsWith(".xes"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return new ArrayList<>();
    }

    private static List<Map.Entry<String, String>> getLogMap(String logsPath) {
        return getAllXesFilesInFolder(logsPath)
                .stream()
                .map(p -> {
                    Path placePath = p.resolveSibling(p.getFileName().toString().replace(".xes", ".promspl"));
                    if (Files.exists(placePath)) {
                        return new AbstractMap.SimpleEntry<>(p.toString(), placePath.toString());
                    }
                    return new AbstractMap.SimpleEntry<>(p.toString(), "");
                })
                .collect(Collectors.toList());
    }

    private static void runAlgorithmForDifferentSettingsAndEventLogs(List<Map.Entry<String, String>> logFilePlacesFilePairs) {
        for (Map.Entry<String, String> entry : logFilePlacesFilePairs) {
            for (Integer placeLimit : new Integer[]{50, 75, 100, 150, 200}) {//, 500, 1000, 2500, 5000}) {
                for (Integer proximity : new Integer[]{5, 7, 12}) {
                    for (PlaceDiscoveryAlgorithmId algorithmId : PlaceDiscoveryAlgorithmId.values()) {
                        runAlgorithmForSpecificSettings(entry, algorithmId, placeLimit, proximity);
                    }
                }
            }
        }
    }

    public static void runAlgorithmForSpecificSettings(Map.Entry<String, String> input, PlaceDiscoveryAlgorithmId pdAlgId, int placeLimit, int proximity) {
        String logPath = input.getKey();
        String placesPath = input.getValue();
        try {
            runAlgorithmForSpecificSettings(pdAlgId, placeLimit, proximity, logPath, placesPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void runAlgorithmForSpecificSettings(PlaceDiscoveryAlgorithmId pdAlgId, int placeLimit, int proximity, String logPath, String placesPath) throws Exception {
        XLog log = LogUtils.readLogFromFile(logPath);
        PlaceSet places = null;
        if (!Strings.isBlank(placesPath))
            places = PlaceUtils.getPlaceSetFromInputStream(new FileInputStream(placesPath));

        System.out.println("Log: " + logPath + "\nAlgorithm: " + pdAlgId + "\nPlace Limit: " + placeLimit + "\nProximity: " + proximity);
        runAlgorithmForSpecificSettings(pdAlgId, placeLimit, proximity, log, places);
    }

    public static void runAlgorithmForSpecificSettings(PlaceDiscoveryAlgorithmId pdAlgId, int placeLimit, int proximity, XLog log, PlaceSet places) {
        CLIPluginContext context = new CLIPluginContext(new CLIContext(), "TestContext");

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);
        parameters.getLpmCombinationParameters().setLpmProximity(proximity);
        parameters.getLpmCombinationParameters().setMinNumPlaces(0);
        parameters.getLpmCombinationParameters().setMaxNumPlaces(Integer.MAX_VALUE);
        parameters.getLpmCombinationParameters().setMinNumTransitions(0);
        parameters.getLpmCombinationParameters().setMaxNumTransitions(Integer.MAX_VALUE);
        parameters.setTimeLimit(600000);
        parameters.getPlaceChooserParameters().setPlaceLimit(placeLimit);
        parameters.setLpmCount(Integer.MAX_VALUE);
        try {
            if (places == null || !pdAlgId.equals(PlaceDiscoveryAlgorithmId.ESTMiner)) {
                setPlaceDiscoveryAlgorithm(parameters, pdAlgId);
                PlaceBasedLPMDiscoveryPlugin.mineLPMs(context, log, parameters);
            } else {
                PlaceBasedLPMDiscoveryPlugin.mineLPMs(context, log, places, parameters);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static void setPlaceDiscoveryAlgorithm(PlaceBasedLPMDiscoveryParameters parameters, PlaceDiscoveryAlgorithmId pdAlgId) {
        PlaceDiscoveryParameters placeDiscoveryParameters;
        if (pdAlgId == PlaceDiscoveryAlgorithmId.ESTMiner)
            placeDiscoveryParameters = new EstMinerPlaceDiscoveryParameters();
        else if (pdAlgId == PlaceDiscoveryAlgorithmId.InductiveMiner)
            placeDiscoveryParameters = new InductiveMinerPlaceDiscoveryParameters();
        else if (pdAlgId == PlaceDiscoveryAlgorithmId.HeuristicMiner)
            placeDiscoveryParameters = new HeuristicMinerPlaceDiscoveryParameters();
        else
            throw new IllegalArgumentException("There is no " + pdAlgId + " algorithm for Place Discovery");

        parameters.setPlaceDiscoveryParameters(placeDiscoveryParameters);
        parameters.setPlaceDiscoveryAlgorithmId(pdAlgId);
    }

    private static void analyzeLogs(String folderName) throws Exception {
        List<Path> xesFilePaths = getAllXesFilesInFolder(folderName);
        int totalEventLogsCount = xesFilePaths.size();
        int counter = 1;
        for (Path eventLogPath : xesFilePaths) {
            System.out.println(eventLogPath.getFileName().toString());
            XLog eventLog = LogUtils.readLogFromFile(eventLogPath.toFile());
            System.out.println("read");
            for (int localDistance : new Integer[]{5, 7, 10, 12, 15, 20}) {
                analyzeLog(eventLog, localDistance);
            }
            System.out.println(counter + " / " + totalEventLogsCount);
            counter++;
        }
    }

    private static void analyzeLog(XLog eventLog, int localDistance) {
        LogAnalyzerParameters parameters = new LogAnalyzerParameters(localDistance);
        LogAnalyzer logAnalyzer = new LogAnalyzer(eventLog, parameters);
        logAnalyzer.getLogStatistics().write("analysis-log-statistics", false);
    }
}