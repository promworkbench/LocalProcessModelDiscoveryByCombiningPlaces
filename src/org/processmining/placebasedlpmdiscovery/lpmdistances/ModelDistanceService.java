package org.processmining.placebasedlpmdiscovery.lpmdistances;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.List;

public interface ModelDistanceService {
    static ModelDistanceService getInstance(XLog log) {
        return new DefaultModelDistanceService(ModelDistanceFactory.getInstance(log));
    }

    double[][] getDistanceMatrix(List<LocalProcessModel> lpmList, ModelDistanceConfig modelDistanceConfig);
}
