package org.processmining.placebasedlpmdiscovery.model.lpmstorage;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

public class LPMsDissolvedToPlaceNetsTreeLPMStorage implements GlobalLPMStorage {
    @Override
    public boolean add(LocalProcessModel lpm) {
        throw new NotImplementedException();
    }

    @Override
    public Collection<LocalProcessModel> getAllLPMs() {
        throw new NotImplementedException();
    }
}
