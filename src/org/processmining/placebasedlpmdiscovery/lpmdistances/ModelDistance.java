package org.processmining.placebasedlpmdiscovery.lpmdistances;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public interface ModelDistance {

    double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2);
}
