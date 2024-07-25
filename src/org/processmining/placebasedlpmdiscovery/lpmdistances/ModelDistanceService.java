package org.processmining.placebasedlpmdiscovery.lpmdistances;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.List;

public interface ModelDistanceService {
    double[][] getDistanceMatrix(List<LocalProcessModel> lpmList, ModelDistanceConfig modelDistanceConfig);
}
