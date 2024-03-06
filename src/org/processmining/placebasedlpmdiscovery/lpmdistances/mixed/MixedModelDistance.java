package org.processmining.placebasedlpmdiscovery.lpmdistances.mixed;

import org.apache.commons.lang3.tuple.Pair;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.List;

public class MixedModelDistance implements ModelDistance {

    private final List<Pair<ModelDistance, Double>> modelDistanceWeightPairs;

    public MixedModelDistance(List<Pair<ModelDistance, Double>> modelDistanceWeightPairs) {
        if (modelDistanceWeightPairs.stream().mapToDouble(Pair::getRight).sum() != 1) {
            throw new IllegalArgumentException("The sum of the weights of the model distances should be 1.");
        }

        this.modelDistanceWeightPairs = modelDistanceWeightPairs;
    }

    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        double distance = 0.0;
        for (Pair<ModelDistance, Double> modelDistanceWeightPair : this.modelDistanceWeightPairs) {
            distance += modelDistanceWeightPair.getRight() * modelDistanceWeightPair.getLeft().calculateDistance(lpm1, lpm2);
        }
        return distance;
    }

    @Override
    public double[][] calculatePairwiseDistance(List<LocalProcessModel> lpms) {
        double[][] distances = new double[lpms.size()][lpms.size()];
        for (Pair<ModelDistance, Double> modelDistanceWeightPair : this.modelDistanceWeightPairs) {
            double[][] tempDistances = modelDistanceWeightPair.getLeft().calculatePairwiseDistance(lpms);
            for (int i = 0; i < distances.length; ++i) {
                for (int j = 0; j < distances.length; ++j) {
                    distances[i][j] = modelDistanceWeightPair.getRight() * tempDistances[i][j];
                }
            }
        }
        return distances;
    }
}
