package org.processmining.placebasedlpmdiscovery.model.lpmstorage;

/**
 * Any class of this type should be able to store the discovered LPMs for the entire event log.
 */
public interface GlobalLPMStorage {

    /**
     * Returns a default global storage
     * @return - a global storage object
     */
    static GlobalLPMStorage getInstance() {
        return new LPMsDissolvedToPlaceNetsTreeLPMStorage();
    }
}
