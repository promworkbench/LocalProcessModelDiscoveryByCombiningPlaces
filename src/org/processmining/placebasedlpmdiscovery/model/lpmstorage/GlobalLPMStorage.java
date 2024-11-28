package org.processmining.placebasedlpmdiscovery.model.lpmstorage;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

/**
 * Any class of this type should be able to store the discovered LPMs for the entire event log.
 */
public interface GlobalLPMStorage {

    /**
     * Returns a default global storage.
     * @return - a global storage object
     */
    static GlobalLPMStorage getInstance() {
        return new LPMsDissolvedToPlaceNetsTreeLPMStorage();
    }

    /**
     * Adds an LPM to the global storage.
     * @param lpm the local process model
     * @return true if @lpm was successfully added, false otherwise
     */
    boolean add(LocalProcessModel lpm);
}
