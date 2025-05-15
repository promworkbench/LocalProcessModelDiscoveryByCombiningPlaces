package org.processmining.lpms.storage;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;
import java.util.Collections;

public class LADALocalTree implements WindowLPMStorage {

    @Override
    public Collection<LocalProcessModel> getLPMs() {
        return Collections.emptyList();
    }
}
