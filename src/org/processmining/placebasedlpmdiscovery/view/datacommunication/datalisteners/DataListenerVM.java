package org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners;

import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;

public interface DataListenerVM {

    void receive(EmittableDataVM data);
}
