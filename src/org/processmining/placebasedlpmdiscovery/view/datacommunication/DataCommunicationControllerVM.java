package org.processmining.placebasedlpmdiscovery.view.datacommunication;

import org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners.DataListenerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.MixedModelDistanceAddDistanceEmittableDataVM;

public interface DataCommunicationControllerVM {

    void registerDataListener(DataListenerVM dataListenerVM, EmittableDataTypeVM dataType);

    void emit(EmittableDataVM data);
}
