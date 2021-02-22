package org.processmining.localprocessmodeldiscoverybycombiningplaces.utils.analysis;

import javafx.util.Pair;
import org.deckfour.xes.model.XLog;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.placediscovery.PlaceDiscoveryAlgorithmId;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Analyzer {

    private final UUID id;

    public SingleExecutionAnalysis totalExecution;
    public SingleExecutionAnalysis lpmDiscoveryExecution;
    Map<Integer, Pair<Integer, Integer>> discoveredLPM; // num places -> (count, count after filtering)
    private final String name;
    private final boolean placeDiscoveryIncluded;
    private final Map<String, ExecutionAnalysisEntry> timers;
    private PlaceDiscoveryAlgorithmId placeDiscoveryAlgorithmId;
    private Pair<Integer, Integer> discoveredPlaces; // (count, count after filtering)
    private final LogStatistics logStatistics;
    private final FPGrowthStatistics fpGrowthStatistics;
    private int countPlacesUsed;
    private int lpmDiscovered;

    public Analyzer(XLog log, boolean placeDiscoveryIncluded) {
        this.id = UUID.randomUUID();
        this.name = String.valueOf(log.getAttributes().get("concept:name"));
        this.logStatistics = new LogStatistics(this.id, log);
        this.fpGrowthStatistics = new FPGrowthStatistics(this.id);

        this.placeDiscoveryIncluded = placeDiscoveryIncluded;
        this.placeDiscoveryAlgorithmId = PlaceDiscoveryAlgorithmId.ESTMiner;

        this.totalExecution = new SingleExecutionAnalysis();
        this.lpmDiscoveryExecution = new SingleExecutionAnalysis();

        this.timers = new HashMap<>();
        this.discoveredLPM = new HashMap<>();
        this.discoveredPlaces = new Pair<>(0, 0);
    }

    public FPGrowthStatistics getFpGrowthStatistics() {
        return this.fpGrowthStatistics;
    }

    public void setCountPlacesUsed(int countPlacesUsed) {
        this.countPlacesUsed = countPlacesUsed;
    }

    public void setLpmDiscovered(int lpmDiscovered) {
        this.lpmDiscovered = lpmDiscovered;
    }

    public void addPlacesDiscovered(int count, int countAfterFilter) {
        this.discoveredPlaces = new Pair<>(count, countAfterFilter);
    }

    public void addLPMsDiscovered(int numPlaces, int count) {
        Pair<Integer, Integer> pair = this.discoveredLPM.getOrDefault(numPlaces, new Pair<>(-1, -1));
        if (pair.getKey() != -1)
            throw new UnsupportedOperationException("The count was already set for " + numPlaces + " to " + pair.getKey());
        this.discoveredLPM.put(numPlaces, new Pair<>(count, pair.getValue()));
    }

    public void addLPMsFiltered(int numPlaces, int count) {
        Pair<Integer, Integer> pair = this.discoveredLPM.getOrDefault(numPlaces, new Pair<>(-1, -1));
        if (pair.getValue() != -1)
            throw new UnsupportedOperationException("The count was already set for " + numPlaces + " to " + pair.getValue());
        this.discoveredLPM.put(numPlaces, new Pair<>(pair.getKey(), count));
    }

    public void setPlaceDiscoveryAlgorithmId(PlaceDiscoveryAlgorithmId algorithmId) {
//        if (!placeDiscoveryIncluded)
//            throw new UnsupportedOperationException("Place discovery is not included in this execution");
//        this.placeDiscoveryAlgorithmId = algorithmId;
    }

    public void startExecution(String name) {
        ExecutionAnalysisEntry entry = timers.getOrDefault(name, new ExecutionAnalysisEntry());
        entry.executionStarted();
        timers.put(name, entry);
    }

    public void stopExecution(String name) {
        ExecutionAnalysisEntry entry = timers.getOrDefault(name, new ExecutionAnalysisEntry());
        entry.executionStopped();
        timers.put(name, entry);
    }

    public void write(String filename, boolean rewrite) {
        writeExecutions(filename + "-executions", rewrite);
        writeGeneral(filename + "-general", rewrite);
        logStatistics.write(filename + "-log-statistics", rewrite);
        fpGrowthStatistics.write(filename + "-fp-growth-statistics", rewrite);
    }

    private void writeExecutions(String filename, boolean rewrite) {
        File file = new File("data/statistics/" + filename + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, !rewrite))) {
            if (rewrite)
                pw.println("Run ID\tMethod Name\tTotal Duration\tAverage Duration\tMinimum Duration\tMax Duration\tNumber of Executions");
            for (String executionName : timers.keySet()) {
                ExecutionAnalysisEntry entry = timers.get(executionName);
                String line = UUID.randomUUID() + "\t" + executionName + "\t" +
                        entry.getSum() + "\t" + entry.getAvg() + "\t" + entry.getMin() + "\t" + entry.getMax() + "\t" + entry.getExecutionCount();
                pw.println(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeGeneral(String filename, boolean rewrite) {
        File file = new File("data/statistics/" + filename + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, !rewrite))) {
            if (rewrite)
                pw.println("ID\tLog Name\tTotal Execution Time\tLPM Discovery Execution Time\t" +
                        "Place Discovery Included\tPlace Discovery Algorithm ID\t" +
                        "Count Places Used\tPlaces discovered\tPlaces discovered - filtered\t" +
                        "LPM discovered\t" +
                        "LPMs with 1 place\tLPMs with 1 place - filtered\tLPMs with 2 places\tLPMs with 2 places - filtered\t" +
                        "LPMs with 3 places\tLPMs with 3 places - filtered\tLPMs with 4 places\tLPMs with 4 places - filtered\t" +
                        "LPMs with 5 places\tLPMs with 5 places - filtered\tTotal Count of LPMs\tTotal Count of LPMs - filtered");

            int totalLPMs = this.discoveredLPM.values().stream().mapToInt(Pair::getKey).sum();
            int totalLPMsFiltered = this.discoveredLPM.values().stream().mapToInt(Pair::getValue).sum();
            pw.println(String.join("\t", String.valueOf(this.id), this.name,
                    String.valueOf(this.totalExecution.getDuration()),
                    String.valueOf(this.lpmDiscoveryExecution.getDuration()),
                    String.valueOf(this.placeDiscoveryIncluded),
                    String.valueOf(this.placeDiscoveryAlgorithmId),
                    String.valueOf(this.countPlacesUsed),
                    String.valueOf(this.discoveredPlaces.getKey()),
                    String.valueOf(this.discoveredPlaces.getValue()),
                    String.valueOf(this.lpmDiscovered),
                    String.valueOf(this.discoveredLPM.getOrDefault(1, new Pair<>(-1, -1)).getKey()),
                    String.valueOf(this.discoveredLPM.getOrDefault(1, new Pair<>(-1, -1)).getValue()),
                    String.valueOf(this.discoveredLPM.getOrDefault(2, new Pair<>(-1, -1)).getKey()),
                    String.valueOf(this.discoveredLPM.getOrDefault(2, new Pair<>(-1, -1)).getValue()),
                    String.valueOf(this.discoveredLPM.getOrDefault(3, new Pair<>(-1, -1)).getKey()),
                    String.valueOf(this.discoveredLPM.getOrDefault(3, new Pair<>(-1, -1)).getValue()),
                    String.valueOf(this.discoveredLPM.getOrDefault(4, new Pair<>(-1, -1)).getKey()),
                    String.valueOf(this.discoveredLPM.getOrDefault(4, new Pair<>(-1, -1)).getValue()),
                    String.valueOf(this.discoveredLPM.getOrDefault(5, new Pair<>(-1, -1)).getKey()),
                    String.valueOf(this.discoveredLPM.getOrDefault(5, new Pair<>(-1, -1)).getValue()),
                    String.valueOf(totalLPMs), String.valueOf(totalLPMsFiltered)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}


