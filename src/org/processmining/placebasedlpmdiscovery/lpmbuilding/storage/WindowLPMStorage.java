package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

/**
 * Stores discovered LPMs for one window.
 */
public interface WindowLPMStorage {

    /**
     * Returns the local process models in the storage.
     * @return a collection of LPMs
     */
    Collection<LocalProcessModel> getLPMs();
}
