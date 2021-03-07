package org.processmining.placebasedlpmdiscovery.utils.analysis;

import javafx.util.Pair;
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

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Runner {

    static Map<String, Pair<String, String>> logIntoInputMap;

    public static void main(String[] args) {
        try {
//            String[] filenames = {"/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/ArtificialBig/artificial1-est.xes",
//                    "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/Choice2variants7activities/choice-2-variants-7-activities.xes",
//                    "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/Road_Traffic_Fine_Management_Process/Road_Traffic_Fine_Management_Process-est.xes",
//                    "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2019/log_IEEE.xes",
//                    "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2012/financial_log.xes",
//                    "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/chapter_8/Chapter_8/repairExample.xes",
//                    "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/chapter_8/Chapter_8/reviewing.xes",
//                    "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/chapter_8/Chapter_8/repairExampleSample2.xes",
//                    "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/chapter_8/Chapter_8/teleclaims.xes"
//            };
//            for (String filename : filenames) {
//                analyzeLog(filename);
//            }
            initializeLogMap();
//            Runtime r = Runtime.getRuntime();
//            System.out.println("Max Memory: " + r.totalMemory());
//            System.out.println("Total Memory: " + r.totalMemory());
//            System.out.println("Free Memory: " + r.freeMemory());
//
//            Scanner scn = new Scanner(System.in);
//            while (true) {
//                findLocalProcessModelsForOneLog(scn);
//
//                System.out.println("Max Memory: " + r.totalMemory());
//                System.out.println("Total Memory: " + r.totalMemory());
//                System.out.println("Free Memory: " + r.freeMemory());
//                r.gc();
//                System.out.println("Total Memory: " + r.totalMemory());
//                System.out.println("Free Memory: " + r.freeMemory());
//            }
            run(logIntoInputMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initializeLogMap() {
        logIntoInputMap = new HashMap<>();
//        logIntoInputMap.put(
//                "Artificial Big",
//                new Pair<>(
//                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/ArtificialBig/artificial1-est.xes",
//                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/ArtificialBig/artificial1.promspl"));
        logIntoInputMap.put(
                "Artificial Small",
                new Pair<>(
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/Choice2variants7activities/choice-2-variants-7-activities.xes",
                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/Choice2variants7activities/places-est.promspl"));
//        logIntoInputMap.put(
//                "HP2017 filtered",
//                new Pair<>(
//                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/hp2017/HP2017_filtered-est.xes",
//                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/hp2017/hp2017-est-miner.promspl"));
//        logIntoInputMap.put(
//                "Road Traffic Fine Management Process",
//                new Pair<>(
//                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/Road_Traffic_Fine_Management_Process/Road_Traffic_Fine_Management_Process-est.xes",
//                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/Road_Traffic_Fine_Management_Process/road-traffic-fine-management-est-miner.promspl"));
//        logIntoInputMap.put(
//                "BPI2019",
//                new Pair<>(
//                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2019/log_IEEE.xes",
//                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2019/places-limit5.promspl"));
//        logIntoInputMap.put(
//                "BPI2012",
//                new Pair<>(
//                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2012/financial_log.xes",
//                        "/home/vikipeeva/Viki/Learning/Masters/Thesis/logs/bpi2012/places-until4transitions-bpi2012.promspl"));
    }

    private static void findLocalProcessModelsForOneLog(Scanner scn) {
        Pair<String, String> logFiles = choosingLog(scn);
        PlaceDiscoveryAlgorithmId placeDiscoveryAlgorithm = choosePlaceDiscoveryAlgorithm(scn);
        System.out.println("Enter place limit:");
        int placeLimit = scn.nextInt();

        runAlgorithm(logFiles, placeDiscoveryAlgorithm, placeLimit, 7);
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
        int ind = 1;
        Map<Integer, String> indexNameMap = new HashMap<>();
        for (Map.Entry<String, ?> entry : logIntoInputMap.entrySet()) {
            indexNameMap.put(ind, entry.getKey());
            ind++;
        }

        while (true) {
            System.out.println("In While True");
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

    private static void run(Map<String, Pair<String, String>> logFilePlacesFileMap) {
//        CLIPluginContext context = new CLIPluginContext(new CLIContext(), "TestContext");
        for (Pair<String, String> entry : logFilePlacesFileMap.values()) {
//            String logPath = entry.getKey();
//            String placesPath = entry.getValue();
//            try {
//                XLog log = LogUtils.readLogFromFile(logPath);
//                PlaceSet places = null;
//                if (placesPath != null)
//                    places = PlaceUtils.getPlaceSetFromInputStream(new FileInputStream(new File(placesPath)));

            for (Integer placeLimit : new Integer[]{50, 75, 100, 150, 200}) {//, 500, 1000, 2500, 5000}) {
                for (Integer proximity : new Integer[]{5, 7, 12}) {
//                    for (PlaceDiscoveryAlgorithmId algorithmId : PlaceDiscoveryAlgorithmId.values()) {
                    runAlgorithm(entry, PlaceDiscoveryAlgorithmId.ESTMiner, placeLimit, proximity);

                    Runtime r = Runtime.getRuntime();
                    System.out.println("Max Memory: " + r.totalMemory());
                    System.out.println("Total Memory: " + r.totalMemory());
                    System.out.println("Free Memory: " + r.freeMemory());
                    r.gc();
                    System.out.println("Total Memory: " + r.totalMemory());
                    System.out.println("Free Memory: " + r.freeMemory());
//                    }
                }
//                    System.out.println("Log: " + logPath + "\nPlace Limit: " + placeLimit);
//                    PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);
//                    parameters.setTimeLimit(90000);
//                    parameters.getPlaceChooserParameters().setPlaceLimit(placeLimit);
//                    try {
//                        if (places == null)
//                            PlaceBasedLPMDiscoveryPlugin.mineLPMs(context, log, parameters);
//                        else
//                            PlaceBasedLPMDiscoveryPlugin.mineLPMs(context, log, places, parameters);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
            }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    public static void runAlgorithm(Pair<String, String> input, PlaceDiscoveryAlgorithmId pdAlgId, int placeLimit, int proximity) {
        CLIPluginContext context = new CLIPluginContext(new CLIContext(), "TestContext");
        String logPath = input.getKey();
        String placesPath = input.getValue();
        try {
            XLog log = LogUtils.readLogFromFile(logPath);
            PlaceSet places = null;
            if (placesPath != null)
                places = PlaceUtils.getPlaceSetFromInputStream(new FileInputStream(new File(placesPath)));

            System.out.println("Log: " + logPath + "\nAlgorithm: " + pdAlgId + "\nPlace Limit: " + placeLimit + "\nProximity: " + proximity);
            PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);
            parameters.getLpmCombinationParameters().setLpmProximity(proximity);
            parameters.getLpmCombinationParameters().setMinNumPlaces(0);
            parameters.getLpmCombinationParameters().setMaxNumPlaces(Integer.MAX_VALUE);
            parameters.getLpmCombinationParameters().setMinNumTransitions(0);
            parameters.getLpmCombinationParameters().setMaxNumTransitions(Integer.MAX_VALUE);
            parameters.setTimeLimit(600000);
            parameters.getPlaceChooserParameters().setPlaceLimit(placeLimit);
//            parameters.getPlaceChooserParameters().setAveragePlaceDegree(10);
            parameters.setLpmCount(Integer.MAX_VALUE);
            try {
                if (places == null || !pdAlgId.equals(PlaceDiscoveryAlgorithmId.ESTMiner)) {
                    setPlaceDiscoveryAlgorithm(parameters, pdAlgId);
                    PlaceBasedLPMDiscoveryPlugin.mineLPMs(context, log, parameters);
                } else
                    PlaceBasedLPMDiscoveryPlugin.mineLPMs(context, log, places, parameters);
            } catch (Throwable e) {
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
        parameters.setPlaceDiscoveryAlgorithmId(pdAlgId);
    }

    private static void analyzeLog(String filename) throws Exception {
//        Scanner scn = new Scanner(System.in);
//        Pair<String, String> logFiles = choosingLog(scn);
        XLog log = LogUtils.readLogFromFile(filename);
        LogAnalyzerParameters parameters = new LogAnalyzerParameters(7);
        LogAnalyzer logAnalyzer = new LogAnalyzer(log, parameters);
        System.out.println(filename);
        System.out.println("trace variants count: " + logAnalyzer.traceVariantCount());
        System.out.println("trace variants total events: " + logAnalyzer.traceVariantTotalEvents());
        System.out.println("activity count: " + logAnalyzer.getActivityCount());
        System.out.println();

//        logAnalyzer.allVsDistinctWindowCount();
    }
}
