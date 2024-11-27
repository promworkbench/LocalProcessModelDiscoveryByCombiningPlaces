package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage;

import org.processmining.placebasedlpmdiscovery.model.lpmstorage.GlobalLPMStorage;

/**
 * A class that is able to transport LPMs from a window to a global storage.
 */
public interface WindowToGlobalLPMStorageTransporter {
    /**
     * Moves LPMs from the window storage to the global storage.
     *
     * @param windowStorage - the window storage from which we want to move the LPMs
     * @param globalStorage - the global storage where we want to store the LPMs
     */
    void move(WindowLPMStorage windowStorage, GlobalLPMStorage globalStorage);

    static WindowToGlobalLPMStorageTransporter getInstance() {
        return new DefaultWindowToGlobalLPMStorageTransporter();
    }
}
