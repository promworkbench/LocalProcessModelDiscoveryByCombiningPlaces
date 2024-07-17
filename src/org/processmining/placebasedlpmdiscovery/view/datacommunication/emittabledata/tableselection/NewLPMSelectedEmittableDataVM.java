package org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.tableselection;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;

public class NewLPMSelectedEmittableDataVM implements EmittableDataVM {

    private final String topic;
    private LocalProcessModel lpm;

    public NewLPMSelectedEmittableDataVM(String topic, LocalProcessModel lpm) {
        this.topic = topic;
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
        return this.topic;
    }

}
