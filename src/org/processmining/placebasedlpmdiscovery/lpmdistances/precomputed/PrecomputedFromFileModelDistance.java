package org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PrecomputedFromFileModelDistance implements ModelDistance {

    private Map<String, Integer> lpmIds;
    private double[][] distance;

    public PrecomputedFromFileModelDistance(PrecomputedFromFileModelDistanceConfig modelDistanceConfig) {
        readDistances(modelDistanceConfig.getFilename());
    }

    private void readDistances(String filename) {
        List<String[]> rowList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineItems = line.split(",");
                rowList.add(lineItems);
            }
        }
        catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }

        this.lpmIds = Stream.iterate(0, x -> x + 1)
                .limit(rowList.size() - 1)
                .collect(Collectors.toMap(ind -> rowList.get(0)[ind], ind -> ind));
        distance = new double[rowList.size() - 1][rowList.size() - 1];
        for (int i = 0; i < rowList.size() - 1; i++) {
            distance[i] = Arrays.stream(Arrays.copyOfRange(rowList.get(i+1), 1, rowList.size()))
                    .mapToDouble(Double::valueOf).toArray();
        }
    }

    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        int indexLpm1 = lpmIds.get(lpm1.getShortString());
        if (indexLpm1 == -1) {
            throw new IllegalArgumentException("Illegal LPM with id " + lpm1.getShortString());
        }

        int indexLpm2 = lpmIds.get(lpm1.getShortString());
        if (indexLpm2 == -1) {
            throw new IllegalArgumentException("Illegal LPM with id " + lpm2.getShortString());
        }

        return distance[indexLpm1][indexLpm2];
    }

    @Override
    public double[][] calculatePairwiseDistance(List<LocalProcessModel> lpms) {
        for (int i = 0; i < lpms.size(); ++i) {
            if (i != this.lpmIds.get(lpms.get(i).getShortString())) {
                return repositionDistances(lpms);
            }
        }
        return this.distance;
    }

    private double[][] repositionDistances(List<LocalProcessModel> lpms) {
        double[][] repositionedDistances = new double[lpms.size()][lpms.size()];
        for (int i = 0; i < lpms.size(); ++i) {
            for (int j = 0; j < lpms.size(); ++j) {
                repositionedDistances[i][j] = distance[this.lpmIds.get(lpms.get(i).getShortString())]
                        [this.lpmIds.get(lpms.get(j).getShortString())];
            }
        }
        return repositionedDistances;
    }
}
