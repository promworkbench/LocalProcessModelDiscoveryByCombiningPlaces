package org.processmining.placebasedlpmdiscovery.view.controllers;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableDataType;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.DefaultLPMDiscoveryResultComponent;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.models.DefaultLPMDiscoveryResultViewModel;
import org.processmining.placebasedlpmdiscovery.view.models.LPMDiscoveryResultViewModel;
import org.processmining.placebasedlpmdiscovery.view.views.LPMDiscoveryResultView;

public class DefaultLPMDiscoveryResultViewController implements LPMDiscoveryResultViewController {

    private final DefaultLPMDiscoveryResultComponent view;
    private final DefaultLPMDiscoveryResultViewModel model;

    @Inject
    public DefaultLPMDiscoveryResultViewController(DataCommunicationController dc, DataCommunicationControllerVM dcVM,
            DefaultLPMDiscoveryResultViewModel model, DefaultLPMDiscoveryResultComponent view) {
        this.view = view;
        this.model = model;

        registerListeners(dc, dcVM);
    }

    private void registerListeners(DataCommunicationController dc, DataCommunicationControllerVM dcVM) {
        dc.registerDataListener(this, EmittableDataType.LPMGroupingFinished);
    }

    @Override
    public void newLPMSelected(LocalProcessModel lpm) {
        this.model.setSelectedLPM(lpm);
        this.view.displaySelectedLPM(this.model);
    }

    @Override
    public DefaultLPMDiscoveryResultComponent getView() {
        return this.view;
    }

    @Override
    public LPMDiscoveryResultViewModel getModel() {
        return this.model;
    }

    @Override
    public void receive(EmittableData data) {
        if (data.getType().equals(EmittableDataType.LPMGroupingFinished)) {

        }
    }

    @Override
    public void receive(EmittableDataVM data) {

    }
}
