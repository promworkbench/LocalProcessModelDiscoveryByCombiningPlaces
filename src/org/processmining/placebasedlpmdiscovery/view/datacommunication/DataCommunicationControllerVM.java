package org.processmining.placebasedlpmdiscovery.view.datacommunication;

import org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners.DataListenerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;

public interface DataCommunicationControllerVM {

    void registerDataListener(DataListenerVM dataListenerVM, EmittableDataTypeVM dataType);

    void receiveEmittableData(EmittableDataVM data);
}
