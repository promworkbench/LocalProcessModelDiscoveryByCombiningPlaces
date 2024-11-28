package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.traversals;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class WindowLPMTreeWrapperAsWindowLPMStorageTraversal implements WindowLPMStorageTraversal {


    private final WindowLPMStorage storage;

    public WindowLPMTreeWrapperAsWindowLPMStorageTraversal(WindowLPMStorage storage) {
        this.storage = storage;
    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public LocalProcessModel next() {
        return null;
    }
}
