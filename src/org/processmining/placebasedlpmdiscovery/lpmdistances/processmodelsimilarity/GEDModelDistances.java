package org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity;

import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.petrinets.analysis.gedsim.algorithms.impl.GraphEditDistanceSimilarityAStar;
import org.processmining.petrinets.analysis.gedsim.params.GraphEditDistanceSimilarityParameters;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.util.List;
import java.util.stream.Collectors;

public class GEDModelDistances implements ModelDistance {
    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        return calculateDistance(
                LocalProcessModelUtils.getPetriNetRepresentation(lpm1),
                LocalProcessModelUtils.getPetriNetRepresentation(lpm2));
    }

    @Override
    public double[][] calculatePairwiseDistance(List<LocalProcessModel> lpms) {
        List<Petrinet> pns = lpms.stream()
                .map(LocalProcessModelUtils::getPetriNetRepresentation)
                .collect(Collectors.toList());

        double[][] distances = new double[lpms.size()][lpms.size()];
        for (int i = 0; i < pns.size(); ++i) {
            for (int j = i; j < pns.size(); ++j) {
                distances[i][j] = calculateDistance(pns.get(i), pns.get(j));
                distances[j][i] = distances[i][j];
            }
        }
        return distances;
    }

    private double calculateDistance(Petrinet pn1, Petrinet pn2) {
        GraphEditDistanceSimilarityAStar<Petrinet> ged = new GraphEditDistanceSimilarityAStar<>(
                new GraphEditDistanceSimilarityParameters());
        return ged.compute(pn1, pn2);
    }
}
