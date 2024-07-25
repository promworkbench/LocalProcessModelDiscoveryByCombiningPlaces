package org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.export;

import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;

public class ExportRequestedEmittableDataVM implements EmittableDataVM {

    private final Object data;

    public Object getData() {
        return data;
    }

    public ExportRequestedEmittableDataVM(Object data) {
        this.data = data;
    }
    @Override
    public EmittableDataTypeVM getType() {
        return EmittableDataTypeVM.ExportRequestedVM;
    }

    @Override
    public String getTopic() {
        return getType().name();
    }
}
