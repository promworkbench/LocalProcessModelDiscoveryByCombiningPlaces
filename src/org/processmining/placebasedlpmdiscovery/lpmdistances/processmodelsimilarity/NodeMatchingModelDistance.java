package org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.utils.HungarianAlgorithm;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.List;
import java.util.Map;

public class NodeMatchingModelDistance implements ModelDistance {
    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        Map<Place, Integer> places1 = PlaceUtils.mapPlacesToIndices(lpm1.getPlaces());
        Map<Place, Integer> places2 = PlaceUtils.mapPlacesToIndices(lpm2.getPlaces());

        // create place cost matrix
        double[][] costMatrix = new double[lpm1.getPlaces().size()][lpm2.getPlaces().size()];
        for (Map.Entry<Place, Integer> entry1 : places1.entrySet()) {
            for (Map.Entry<Place, Integer> entry2 : places2.entrySet()) {
                costMatrix[entry1.getValue()][entry2.getValue()] =
                        PlaceUtils.computePlaceMatchingCost(entry1.getKey(), entry2.getKey());
            }
        }

        // compute optimal matching
        HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(costMatrix);
        int[][] assignments = hungarianAlgorithm.findOptimalAssignment();
        double totalCost = 0;
        for (int i = 0; i < assignments.length; ++i) {
            int iAssignment = assignments[i][1];
            if (iAssignment == -1) {
                continue;
            }
            totalCost += costMatrix[i][iAssignment];
        }

        return 2 * totalCost / (places1.size() + places2.size());
    }

    @Override
    public double[][] calculatePairwiseDistance(List<LocalProcessModel> lpms) {
        double[][] distances = new double[lpms.size()][lpms.size()];
        for (int i = 0; i < lpms.size(); ++i) {
            for (int j = 0; j < lpms.size(); ++i) {
                distances[i][j] = this.calculateDistance(lpms.get(i), lpms.get(j));
            }
        }
        return distances;
    }
}
