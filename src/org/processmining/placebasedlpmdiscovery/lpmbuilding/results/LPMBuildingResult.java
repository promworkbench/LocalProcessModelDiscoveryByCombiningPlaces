package org.processmining.placebasedlpmdiscovery.lpmbuilding.results;

import org.processmining.placebasedlpmdiscovery.model.lpmstorage.GlobalLPMStorage;

/**
 * Wraps the result produced by {@link org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlg}.
 */
public interface LPMBuildingResult {

    /**
     * Returns the LPM storage populated by
     * {@link org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlg}.
     * @return - a global storage object
     */
    GlobalLPMStorage getStorage();

}
