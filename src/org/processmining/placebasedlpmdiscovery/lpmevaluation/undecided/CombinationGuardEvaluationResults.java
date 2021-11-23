package org.processmining.placebasedlpmdiscovery.lpmevaluation.undecided;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class CombinationGuardEvaluationResults {

    private List<Entry<String, LocalProcessModelEvaluationResults>> resultsMap;

    public CombinationGuardEvaluationResults() {
        this.resultsMap = new ArrayList<>();
    }

    public List<Entry<String, LocalProcessModelEvaluationResults>> getResultsMap() {
        return resultsMap;
    }

    public void addResult(String key, LocalProcessModelEvaluationResults results) {
        this.resultsMap.add(new SimpleEntry<>(key, results));
    }

    public void extractToFile(File file) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Entry<String, LocalProcessModelEvaluationResults> entry : resultsMap) {
                StringBuilder sb = new StringBuilder();
                sb.append(entry.getKey()).append(":").append("\n");
                sb.append(entry.getValue());
                writer.write(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
