package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.traversals.WindowLPMStorageTraversal;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.lpmstorage.GlobalLPMStorage;

public class DefaultWindowToGlobalLPMStorageTransporter implements WindowToGlobalLPMStorageTransporter{
    @Override
    public void move(WindowLPMStorage windowStorage, GlobalLPMStorage globalStorage) {
        WindowLPMStorageTraversal traversal = WindowLPMStorageTraversal.getInstance(windowStorage);
        while (traversal.hasNext()) {
            LocalProcessModel lpm = traversal.next();
            globalStorage.add(lpm);
        }
    }
}
