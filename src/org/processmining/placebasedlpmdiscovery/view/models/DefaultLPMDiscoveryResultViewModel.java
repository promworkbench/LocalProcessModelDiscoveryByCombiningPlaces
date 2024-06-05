package org.processmining.placebasedlpmdiscovery.view.models;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collection;

public class DefaultLPMDiscoveryResultViewModel implements LPMDiscoveryResultViewModel {

    private final LPMDiscoveryResult result;
    private LocalProcessModel selectedLPM;

    @Inject
    public DefaultLPMDiscoveryResultViewModel(LPMDiscoveryResult result) {
        this.result = result;
    }

    @Override
    public Collection<LocalProcessModel> getLPMs() {
        return result.getAllLPMs();
    }

    @Override
    public void setLPMs(Collection<LocalProcessModel> lpms) {

    }

    @Override
    public LocalProcessModel getSelectedLPM() {
        return this.selectedLPM;
    }

    public void setSelectedLPM(LocalProcessModel selectedLPM) {
        this.selectedLPM = selectedLPM;
    }
}
