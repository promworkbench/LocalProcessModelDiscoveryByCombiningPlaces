package org.processmining.placebasedlpmdiscovery.analysis.statistics;

import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryAlgorithmId;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.UUID;

public class ParameterStatistics implements IStatistics {

    private final UUID executionId;

    private int proximity;
    private int countPlacesLimit;
    private PlaceDiscoveryAlgorithmId placeDiscoveryAlgorithmId;
    private boolean placeDiscoveryIncluded;

    public ParameterStatistics(UUID executionId) {
        this.executionId = executionId;
        this.placeDiscoveryAlgorithmId = PlaceDiscoveryAlgorithmId.ESTMiner;
    }

    public void setProximity(int proximity) {
        this.proximity = proximity;
    }

    public void setCountPlacesLimit(int countPlacesLimit) {
        this.countPlacesLimit = countPlacesLimit;
    }

    public void setPlaceDiscoveryAlgorithmId(PlaceDiscoveryAlgorithmId placeDiscoveryAlgorithmId) {
        this.placeDiscoveryAlgorithmId = placeDiscoveryAlgorithmId;
    }

    public void setPlaceDiscoveryIncluded(boolean placeDiscoveryIncluded) {
        this.placeDiscoveryIncluded = placeDiscoveryIncluded;
    }

    @Override
    public void write(String filename, boolean rewrite) {
        File file = new File(filename + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, !rewrite))) {
            if (rewrite)
                pw.println("ID\tPlace Discovery Included\tPlace Discovery Algorithm ID\t" +
                        "Count Places Limit\tProximity");

            pw.println(String.join("\t", String.valueOf(this.executionId),
                    String.valueOf(this.placeDiscoveryIncluded),
                    String.valueOf(this.placeDiscoveryAlgorithmId),
                    String.valueOf(this.countPlacesLimit),
                    String.valueOf(this.proximity)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
