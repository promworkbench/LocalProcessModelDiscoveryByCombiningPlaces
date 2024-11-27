package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.traversals;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

/**
 * The classes of this type are used to traverse an LPM storage for a single window.
 */
public interface WindowLPMStorageTraversal {
    /**
     * Returns if there is a next LPM in the storage.
     * @return true if there is a next LPM, false otherwise
     */
    boolean hasNext();

    /**
     * Returns the next LPM in the storage.
     * @return next LPM in the storage.
     */
    LocalProcessModel next();
}
