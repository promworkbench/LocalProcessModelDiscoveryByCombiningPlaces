package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.traversals;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMTreeWrapperAsWindowLPMStorage;
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

    /**
     * Returns a default traversal for the given storage.
     * @param storage the storage for which we want to get a traversal object
     * @return - a traversal object
     */
    static WindowLPMStorageTraversal getInstance(WindowLPMStorage storage) {
        if (storage instanceof WindowLPMTreeWrapperAsWindowLPMStorage) {
            return new WindowLPMTreeWrapperAsWindowLPMStorageTraversal(storage);
        }
        throw new NotImplementedException("There exists no traversal for a storage of type: " + storage.getClass());
    }
}
