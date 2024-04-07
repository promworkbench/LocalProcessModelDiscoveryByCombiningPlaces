package org.processmining.placebasedlpmdiscovery.lpmdistances;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.List;

public interface ModelDistance {

    double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2);

    double[][] calculatePairwiseDistance(List<LocalProcessModel> lpms);
}
