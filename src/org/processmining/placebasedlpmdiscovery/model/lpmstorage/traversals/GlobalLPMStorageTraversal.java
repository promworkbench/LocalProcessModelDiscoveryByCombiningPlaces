package org.processmining.placebasedlpmdiscovery.model.lpmstorage.traversals;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

/**
 * The classes of this type are used to traverse a global LPM storage.
 */
public interface GlobalLPMStorageTraversal {

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