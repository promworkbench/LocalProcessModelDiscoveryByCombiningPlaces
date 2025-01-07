package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.traversals;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMTreeWrapperAsWindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Optional;

public class WindowLPMTreeWrapperAsWindowLPMStorageTraversal implements WindowLPMStorageTraversal {


    private final Collection<LocalProcessModel> lpms;

    public WindowLPMTreeWrapperAsWindowLPMStorageTraversal(WindowLPMTreeWrapperAsWindowLPMStorage storage) {
        this.lpms = storage.getAllValidLPMs();
    }

    @Override
    public boolean hasNext() {
        return !lpms.isEmpty();
    }

    @Override
    public LocalProcessModel next() {
        Optional<LocalProcessModel> lpm = lpms.stream().findFirst();
        if (lpm.isPresent()) {
            lpms.remove(lpm.get());
            return lpm.get();
        }
        throw new NoSuchElementException("There are no more elements.");
    }
}
