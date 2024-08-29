package org.processmining.placebasedlpmdiscovery.view.models;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.service.lpms.LPMSetService;

import java.util.Collection;

public class DefaultLPMDiscoveryResultViewModel implements LPMDiscoveryResultViewModel {

    private final LPMSetService lpmSetService;
    private LocalProcessModel selectedLPM;

    @Inject
    public DefaultLPMDiscoveryResultViewModel(LPMSetService lpmSetService) {
        this.lpmSetService = lpmSetService;
    }

    @Override
    public Collection<LocalProcessModel> getLPMs() {
        return this.lpmSetService.getLPMSet().getAllLPMs();
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
