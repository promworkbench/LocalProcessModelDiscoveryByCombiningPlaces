package org.processmining.placebasedlpmdiscovery.utils.analysis;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;

public class PlaceStatistics {

    private final UUID executionId;

    private double averageCountInputTransitions;
    private double averageCountOutputTransitions;
    private int countPlaces;

    public PlaceStatistics(UUID executionId) {
        this.executionId = executionId;
    }

    public void initializePlaceStatistics(Set<Place> places) {
        this.averageCountInputTransitions = places.stream()
                .mapToInt(p -> p.getInputTransitions().size()).sum() * 1.0 / places.size();
        this.averageCountOutputTransitions = places.stream()
                .mapToInt(p -> p.getOutputTransitions().size()).sum() * 1.0 / places.size();
        this.countPlaces = places.size();
    }

    public void write(String filename, boolean rewrite) {
        File file = new File(filename + ".csv");
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, !rewrite))) {
            if (rewrite)
                pw.println("ID\tCount Places\tAverage number of input transitions\t" +
                        "Average number of output transitions");
            pw.println(String.join("\t", String.valueOf(executionId),
                    String.valueOf(this.countPlaces),
                    String.valueOf(this.averageCountInputTransitions),
                    String.valueOf(this.averageCountOutputTransitions)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
