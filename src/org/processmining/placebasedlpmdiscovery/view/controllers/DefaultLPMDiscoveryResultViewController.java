package org.processmining.placebasedlpmdiscovery.view.controllers;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableDataType;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.DefaultLPMDiscoveryResultComponent;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.componentchange.LPMSetDisplayComponentChangeEmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.models.DefaultLPMDiscoveryResultViewModel;
import org.processmining.placebasedlpmdiscovery.view.models.LPMDiscoveryResultViewModel;

public class DefaultLPMDiscoveryResultViewController implements LPMDiscoveryResultViewController {

    private final DataCommunicationController dc;
    private final DataCommunicationControllerVM dcVM;

    private final DefaultLPMDiscoveryResultComponent view;
    private final DefaultLPMDiscoveryResultViewModel model;

    @Inject
    public DefaultLPMDiscoveryResultViewController(DataCommunicationController dc, DataCommunicationControllerVM dcVM,
            DefaultLPMDiscoveryResultViewModel model, DefaultLPMDiscoveryResultComponent view) {
        this.dc = dc;
        this.dcVM = dcVM;

        this.view = view;
        this.model = model;

        registerListeners();
    }

    private void registerListeners() {
        this.dc.registerDataListener(this, EmittableDataType.LPMGroupingFinished);

        this.dcVM.registerDataListener(this.view, EmittableDataTypeVM.LPMSetDisplayComponentChange);
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
            this.dcVM.emit(new LPMSetDisplayComponentChangeEmittableDataVM(LPMSetDisplayComponent.Type.Grouped));
        }
    }

    @Override
    public void receive(EmittableDataVM data) {

    }
}
