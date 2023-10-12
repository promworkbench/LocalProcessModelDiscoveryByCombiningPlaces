package org.processmining.placebasedlpmdiscovery.view.models;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.main.MultipleLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

public class DefaultMultipleLPMDiscoveryResultsViewModel implements MultipleLPMDiscoveryResultsViewModel {

    private Collection<LocalProcessModel> activeLPMs;
    private final MultipleLPMDiscoveryResults result;

    public DefaultMultipleLPMDiscoveryResultsViewModel(MultipleLPMDiscoveryResults result) {
        this.result = result;
        this.activeLPMs = this.result.getResult("Set 1").getAllLPMs();
    }

    @Override
    public LPMDiscoveryResult getLPMDiscoveryResult(String name) {
        return this.result.getResult(name);
    }

    @Override
    public Collection<LocalProcessModel> getLPMs() {
        return this.activeLPMs;
    }

    @Override
    public void setLPMs(Collection<LocalProcessModel> lpms) {
        this.activeLPMs = lpms;
    }
}
