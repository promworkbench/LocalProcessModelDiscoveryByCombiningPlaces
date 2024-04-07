package org.processmining.placebasedlpmdiscovery.lpmdistances.mixed;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.List;
import java.util.Map;

public class MixedModelDistance implements ModelDistance {

    private final Map<String, Double> weights;
    private final Map<String, ModelDistance> distances;

    public MixedModelDistance(Map<String, Double> weights, Map<String, ModelDistance> distances) {
        if (weights.size() != distances.size() || !weights.keySet().containsAll(distances.keySet())) {
            throw new IllegalArgumentException("The provided distances and weights do not match.");
        }
        if (weights.values().stream().mapToDouble(v -> v).sum() != 1) {
            throw new IllegalArgumentException("The sum of the weights of the model distances should be 1.");
        }
        this.weights = weights;
        this.distances = distances;
    }

    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        double distance = 0.0;
        for (String distKey : weights.keySet()) {
            distance += weights.get(distKey) * distances.get(distKey).calculateDistance(lpm1, lpm2);
        }
        return distance;
    }

    @Override
    public double[][] calculatePairwiseDistance(List<LocalProcessModel> lpms) {
        double[][] distances = new double[lpms.size()][lpms.size()];
        for (String distKey : this.distances.keySet()) {
            double[][] tempDistances = this.distances.get(distKey).calculatePairwiseDistance(lpms);
            for (int i = 0; i < distances.length; ++i) {
                for (int j = 0; j < distances.length; ++j) {
                    distances[i][j] = this.weights.get(distKey) * tempDistances[i][j];
                }
            }
        }
        return distances;
    }
}
