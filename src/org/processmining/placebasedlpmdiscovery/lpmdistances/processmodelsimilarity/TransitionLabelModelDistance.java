package org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TransitionLabelModelDistance implements ModelDistance {
    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        Set<String> lpm1Labels = lpm1.getTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet());
        Set<String> lpm2Labels = lpm2.getTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet());
        int intersectionSize = Sets.intersection(lpm1Labels, lpm2Labels).size();
        return  2.0 * intersectionSize / (lpm1Labels.size() + lpm2Labels.size());
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
