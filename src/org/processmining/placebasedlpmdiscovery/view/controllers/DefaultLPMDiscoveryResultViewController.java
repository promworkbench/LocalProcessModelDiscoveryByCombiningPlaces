package org.processmining.placebasedlpmdiscovery.view.controllers;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.DefaultLPMDiscoveryResultComponent;
import org.processmining.placebasedlpmdiscovery.view.models.DefaultLPMDiscoveryResultViewModel;

public class DefaultLPMDiscoveryResultViewController implements LPMDiscoveryResultViewController {

    private final DefaultLPMDiscoveryResultComponent view;
    private final DefaultLPMDiscoveryResultViewModel model;

    public DefaultLPMDiscoveryResultViewController(DefaultLPMDiscoveryResultComponent view,
                                                   DefaultLPMDiscoveryResultViewModel model) {
        this.view = view;
        this.model = model;
    }

    @Override
    public void newLPMSelected(LocalProcessModel lpm) {
        this.model.setSelectedLPM(lpm);
        this.view.displaySelectedLPM(this.model);
    }
}
