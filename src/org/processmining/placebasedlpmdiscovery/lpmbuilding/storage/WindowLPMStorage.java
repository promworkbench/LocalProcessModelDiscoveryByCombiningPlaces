package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage;

import org.processmining.lpms.storage.LADALocalTree;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

/**
 * Stores discovered LPMs for one window.
 */
public interface WindowLPMStorage { // QA: Should not be called like this, the name should explain the type of storage.

    static WindowLPMStorage lada() {
        return new LADALocalTree();
    }

    /**
     * Returns the local process models in the storage.
     * @return a collection of LPMs
     */
    Collection<LocalProcessModel> getLPMs();
}
