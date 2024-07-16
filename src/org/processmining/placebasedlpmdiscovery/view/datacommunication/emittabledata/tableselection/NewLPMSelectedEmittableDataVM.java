package org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.tableselection;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;

public class NewLPMSelectedEmittableDataVM implements EmittableDataVM {

    private LocalProcessModel lpm;

    public NewLPMSelectedEmittableDataVM(LocalProcessModel lpm) {
        this.lpm = lpm;
    }

    public LocalProcessModel getLpm() {
        return lpm;
    }

    @Override
    public EmittableDataTypeVM getType() {
        return EmittableDataTypeVM.NewLPMSelectedVM;
    }

    @Override
    public String getTopic() {
        return this.getType().name();
    }

}
