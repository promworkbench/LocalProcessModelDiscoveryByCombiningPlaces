package org.processmining.lpms.storage;

import org.apache.commons.lang3.NotImplementedException;
import org.processmining.lpms.model.LPM;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;
import java.util.Collections;

public class LADALocalTree implements WindowLPMStorage {

    @Override
    public Collection<LocalProcessModel> getLPMs() {
        return Collections.emptyList();
    }

    @Override
    public boolean hasNext() {
        throw new NotImplementedException();
    }

    @Override
    public LPM next() {
        throw new NotImplementedException();
    }
}
