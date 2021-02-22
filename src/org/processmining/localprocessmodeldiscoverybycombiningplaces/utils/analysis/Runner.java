package org.processmining.localprocessmodeldiscoverybycombiningplaces.utils.analysis;

import javafx.util.Pair;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.cli.CLIContext;
import org.processmining.contexts.cli.CLIPluginContext;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.loganalyzer.LogAnalyzer;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.loganalyzer.LogAnalyzerParameters;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.serializable.PlaceSet;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.PlaceDiscoveryAlgorithmId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.parameters.PlaceDiscoveryParameters;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.plugins.mining.PlaceBasedLPMDiscoveryPlugin;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.utils.LogUtils;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.utils.PlaceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Runner {
    public static void main(String[] args) {
        try {
            findLocalProcessModelsForOneLog();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void findLocalProcessModelsForOneLog() {
        Scanner scn = new Scanner(System.in);
        Pair<String, String> logFiles = choosingLog(scn);
        PlaceDiscoveryAlgorithmId placeDiscoveryAlgorithm = choosePlaceDiscoveryAlgorithm(scn);
        System.out.println("Enter place limit:");
        int placeLimit = scn.nextInt();

        runAlgorithm(logFiles, placeDiscoveryAlgorithm, placeLimit);
    }

    /**
     * The user can choose which place discovery algorithm is used for the places
     *
     * @return: the id of the place discovery algorithm
     */
    private static PlaceDiscoveryAlgorithmId choosePlaceDiscoveryAlgorithm(Scanner scn) {
        Map<String, PlaceDiscoveryAlgorithmId> placeDiscoveryAlgorithms = new HashMap<>();
        placeDiscoveryAlgorithms.put("E", PlaceDiscoveryAlgorithmId.ESTMiner);
        placeDiscoveryAlgorithms.put("H", PlaceDiscoveryAlgorithmId.HeuristicMiner);
        placeDiscoveryAlgorithms.put("I", PlaceDiscoveryAlgorithmId.InductiveMiner);

        System.out.println("Choose place discovery algorithm:" +
                " E (eST Miner), H (Heuristic Miner), I (Inductive Miner), N (None)");
        return placeDiscoveryAlgorithms.getOrDefault(scn.next(), PlaceDiscoveryAlgorithmId.ESTMiner);
    }

    /**
     * The user can choose a log
     *
     * @return: the names of the files where the log and a set of places generated from eST Miner are stored
     */
    private static Pair<String, String> choosingLog(Scanner scn) {
        Map<String, Pair<String, String>> logIntoInputMap = new HashMap<>();
        logIntoInputMap.put(
                "Artificial Big",
                new Pair<>(
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/ArtificialBig/artificial1-est.xes",
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/ArtificialBig/artificial1.promspl"));
        logIntoInputMap.put(
                "Artificial Small",
                new Pair<>(
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/Choice2variants7activities/choice-2-variants-7-activities.xes",
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/Choice2variants7activities/places-est.promspl"));
        logIntoInputMap.put(
                "HP2017 filtered",
                new Pair<>(
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/hp2017/HP2017_filtered-est.xes",
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/hp2017/hp2017-est-miner.promspl"));
        logIntoInputMap.put(
                "Road Traffic Fine Management Process",
                new Pair<>(
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/Road_Traffic_Fine_Management_Process/Road_Traffic_Fine_Management_Process-est.xes",
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/Road_Traffic_Fine_Management_Process/road-traffic-fine-management-est-miner.promspl"));
        logIntoInputMap.put(
                "BPI2019",
                new Pair<>(
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2019/log_IEEE.xes",
                        null));
        logIntoInputMap.put(
                "BPI2012",
                new Pair<>(
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2012/financial_log.xes",
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2012/places-until4transitions-bpi2012.promspl"));

        int ind = 1;
        Map<Integer, String> indexNameMap = new HashMap<>();
        for (Map.Entry<String, ?> entry : logIntoInputMap.entrySet()) {
            indexNameMap.put(ind, entry.getKey());
            ind++;
        }

        while (true) {
            printChoices(indexNameMap);
            int choice = scn.nextInt();
            if (choice == 0)
                return null;
            if (indexNameMap.containsKey(choice)) {
                return logIntoInputMap.get(indexNameMap.get(choice));
            } else {
                System.out.println("Invalid input. Try again.");
            }
        }
    }

    private static void printChoices(Map<Integer, String> indexNameMap) {
        System.out.println("Choose one of the logs:");
        for (Map.Entry<Integer, String> entry : indexNameMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static void run(Map<String, String> logFilePlacesFileMap) {
        CLIPluginContext context = new CLIPluginContext(new CLIContext(), "TestContext");
        for (Map.Entry<String, String> entry : logFilePlacesFileMap.entrySet()) {
            String logPath = entry.getKey();
            String placesPath = entry.getValue();
            try {
                XLog log = LogUtils.readLogFromFile(logPath);
                PlaceSet places = null;
                if (placesPath != null)
                    places = PlaceUtils.getPlaceSetFromInputStream(new FileInputStream(new File(placesPath)));

                for (Integer placeLimit : new Integer[]{50, 100, 250}) {//, 500, 1000, 2500, 5000}) {
                    System.out.println("Log: " + logPath + "\nPlace Limit: " + placeLimit);
                    PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);
                    parameters.setTimeLimit(180000);
                    parameters.getPlaceChooserParameters().setPlaceLimit(placeLimit);
                    try {
                        if (places == null)
                            PlaceBasedLPMDiscoveryPlugin.mineLPMs(context, log, parameters);
                        else
                            PlaceBasedLPMDiscoveryPlugin.mineLPMs(context, log, places, parameters);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void runAlgorithm(Pair<String, String> input, PlaceDiscoveryAlgorithmId pdAlgId, int placeLimit) {
        CLIPluginContext context = new CLIPluginContext(new CLIContext(), "TestContext");
        String logPath = input.getKey();
        String placesPath = input.getValue();
        try {
            XLog log = LogUtils.readLogFromFile(logPath);
            PlaceSet places = null;
            if (placesPath != null)
                places = PlaceUtils.getPlaceSetFromInputStream(new FileInputStream(new File(placesPath)));

            System.out.println("Log: " + logPath + "\nPlace Limit: " + placeLimit);
            PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);
            parameters.getLpmCombinationParameters().setLpmProximity(7);
            parameters.setTimeLimit(600000);
            parameters.getPlaceChooserParameters().setPlaceLimit(placeLimit);
            parameters.setLpmCount(Integer.MAX_VALUE);
            try {
                if (places == null || !pdAlgId.equals(PlaceDiscoveryAlgorithmId.ESTMiner)) {
                    setPlaceDiscoveryAlgorithm(parameters, pdAlgId);
                    PlaceBasedLPMDiscoveryPlugin.mineLPMs(context, log, parameters);
                } else
                    PlaceBasedLPMDiscoveryPlugin.mineLPMs(context, log, places, parameters);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
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
    }

    private static void analyzeLog() throws Exception {
        Scanner scn = new Scanner(System.in);
        Pair<String, String> logFiles = choosingLog(scn);
        XLog log = LogUtils.readLogFromFile(logFiles.getKey());
        LogAnalyzerParameters parameters = new LogAnalyzerParameters();
        parameters.setDistanceLimit(7);
        LogAnalyzer logAnalyzer = new LogAnalyzer(log, parameters);
        logAnalyzer.allVsDistinctWindowCount();
    }
}
