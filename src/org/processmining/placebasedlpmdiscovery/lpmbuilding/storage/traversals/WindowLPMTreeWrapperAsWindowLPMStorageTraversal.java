package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.traversals;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMTreeWrapperAsWindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.List;

public class WindowLPMTreeWrapperAsWindowLPMStorageTraversal implements WindowLPMStorageTraversal {


    private final WindowLPMTreeWrapperAsWindowLPMStorage storage;

    private final List<LocalProcessModel> lpms;

    public WindowLPMTreeWrapperAsWindowLPMStorageTraversal(WindowLPMTreeWrapperAsWindowLPMStorage storage) {
        this.storage = storage;
        this.lpms = this.storage.getAllValidLPMs();
    }

    @Override
    public boolean hasNext() {
        return !lpms.isEmpty();
    }

    @Override
    public LocalProcessModel next() {
        return lpms.remove(0);
    }
}
