package org.processmining.placebasedlpmdiscovery.utils.analysis;

import javafx.util.Pair;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryAlgorithmId;
import org.processmining.placebasedlpmdiscovery.utils.ProjectProperties;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Analyzer {

    private static final int VERSION = 1;

    private final UUID id;
    private String writeDestination = "data/statistics/";

    public SingleExecutionAnalysis totalExecution;
    public SingleExecutionAnalysis lpmDiscoveryExecution;
    Map<Integer, Pair<Integer, Integer>> discoveredLPM; // num places -> (count, count after filtering)
    private final String name;
    private final boolean placeDiscoveryIncluded;
    private final Map<String, ExecutionAnalysisEntry> timers;
    private PlaceDiscoveryAlgorithmId placeDiscoveryAlgorithmId;
    private final PlaceStatistics placeStatistics;
    private final LogStatistics logStatistics;
    private final FPGrowthStatistics fpGrowthStatistics;
    private int discoveredPlaces; // (count, count after filtering)
    private int countPlacesUsed;
    private int lpmDiscovered;
    private double placesAverageOutDegree;
    private int proximity;
    private int allLpmDiscovered;

    public Analyzer(XLog log, boolean placeDiscoveryIncluded) {
        this.id = UUID.randomUUID();
        this.name = String.valueOf(log.getAttributes().get("concept:name"));
        this.logStatistics = new LogStatistics(this.id, log);
        this.fpGrowthStatistics = new FPGrowthStatistics(this.id);

        this.placeDiscoveryIncluded = placeDiscoveryIncluded;
        this.placeDiscoveryAlgorithmId = PlaceDiscoveryAlgorithmId.ESTMiner;
        this.placeStatistics = new PlaceStatistics(this.id);

        this.totalExecution = new SingleExecutionAnalysis();
        this.lpmDiscoveryExecution = new SingleExecutionAnalysis();

        this.timers = new HashMap<>();
        this.discoveredLPM = new HashMap<>();
        this.discoveredPlaces = 0;
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

    public void addPlacesDiscovered(int count) {
        this.discoveredPlaces = count;
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
        this.placeDiscoveryAlgorithmId = algorithmId;
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

    public void changeWriteDestination(String writeDestination) {
        this.writeDestination = writeDestination;
    }

    public void write() {
        boolean rewrite = false;
        if (ProjectProperties.ANALYSIS_WRITE_VERSION_VALUE != VERSION) {
            ProjectProperties.updateIntegerProperty(ProjectProperties.ANALYSIS_WRITE_VERSION_KEY, VERSION);
            rewrite = true;
        }
        String suffix = "-v" + VERSION;

        writeExecutions(writeDestination + "analysis-executions" + suffix, rewrite);
        writeGeneral(writeDestination + "analysis-general" + suffix, rewrite);
        logStatistics.write(writeDestination + "analysis-log-statistics" + suffix, rewrite);
        fpGrowthStatistics.write(writeDestination + "analysis-fp-growth-statistics" + suffix, rewrite);
        placeStatistics.write(writeDestination + "analysis-place-statistics" + suffix, rewrite);
    }

    private void writeExecutions(String filename, boolean rewrite) {
        File file = new File(filename + ".csv");
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
        File file = new File(filename + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, !rewrite))) {
            if (rewrite)
                pw.println("ID\tLog Name\tTotal Execution Time\tLPM Discovery Execution Time\t" +
                        "Place Discovery Included\tPlace Discovery Algorithm ID\t" +
                        "Count Places Used\tPlaces discovered\t" +
                        "Places Average Out Degree\t" +
                        "LPM discovered\tAll LPM Discovered\tProximity\tTimestamp");

            pw.println(String.join("\t", String.valueOf(this.id), this.name,
                    String.valueOf(this.totalExecution.getDuration()),
                    String.valueOf(this.lpmDiscoveryExecution.getDuration()),
                    String.valueOf(this.placeDiscoveryIncluded),
                    String.valueOf(this.placeDiscoveryAlgorithmId),
                    String.valueOf(this.countPlacesUsed),
                    String.valueOf(this.discoveredPlaces),
                    String.valueOf(this.placesAverageOutDegree),
                    String.valueOf(this.lpmDiscovered),
                    String.valueOf(this.allLpmDiscovered),
                    String.valueOf(this.proximity),
                    String.valueOf(Date.from(Instant.now()))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void setPlacesAverageOutDegree(double averageNodeOutDegree) {
        this.placesAverageOutDegree = averageNodeOutDegree;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }

    public PlaceStatistics getPlaceStatistics() {
        return placeStatistics;
    }

    public void setAllLpmDiscovered(int size) {
        this.allLpmDiscovered = size;
    }
}


