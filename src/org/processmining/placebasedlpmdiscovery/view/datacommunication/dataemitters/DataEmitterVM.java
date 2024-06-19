package org.processmining.placebasedlpmdiscovery.view.datacommunication.dataemitters;

import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;

public interface DataEmitterVM {
    void emit(EmittableDataVM data);
}
