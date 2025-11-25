package org.processmining.lpms.storage;

import org.apache.commons.lang3.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.Collection;
import java.util.Collections;

public class LADALocalTree implements WindowLPMStorage<LocalProcessModel> {

    @Override
    public Collection<LocalProcessModel> getLPMs() {
        return Collections.emptyList();
    }

    @Override
    public Collection<LocalProcessModel> removeLPMsFor(Activity activity, int position) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addLPMFor(Activity activity, int position, LocalProcessModel lpm) {
        throw new NotImplementedException();
    }

    @Override
    public boolean hasNext() {
        throw new NotImplementedException();
    }

    @Override
    public LocalProcessModel next() {
        throw new NotImplementedException();
    }
}
