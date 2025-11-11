package org.processmining.lpms.storage;

import org.apache.commons.lang3.NotImplementedException;
import org.processmining.lpms.model.LPM;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.Collection;
import java.util.Collections;

public class LADALocalTree implements WindowLPMStorage {

    @Override
    public Collection<LocalProcessModel> getLPMs() {
        return Collections.emptyList();
    }

    @Override
    public Collection<LPM> removeLPMsFor(Activity activity, int position) {
        throw new NotImplementedException();
    }

    @Override
    public boolean addLPMFor(Activity activity, int position, LPM lpm) {
        throw new NotImplementedException();
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
