package org.processmining.placebasedlpmdiscovery.view.models;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

public class DefaultLPMDiscoveryResultViewModel implements LPMDiscoveryResultViewModel {

    private final LPMDiscoveryResult result;

    public DefaultLPMDiscoveryResultViewModel(LPMDiscoveryResult result) {
        this.result = result;
    }

    @Override
    public Collection<LocalProcessModel> getLPMs() {
        return null;
    }

    @Override
    public void setLPMs(Collection<LocalProcessModel> lpms) {

    }

}
