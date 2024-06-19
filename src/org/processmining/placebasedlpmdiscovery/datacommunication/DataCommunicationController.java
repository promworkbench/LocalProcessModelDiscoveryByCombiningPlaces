package org.processmining.placebasedlpmdiscovery.datacommunication;

import org.processmining.placebasedlpmdiscovery.datacommunication.datalisteners.DataListener;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableDataType;

public interface DataCommunicationController {

    void registerDataListener(DataListener dataListener, EmittableDataType dataType);

    void receiveEmittableData(EmittableData data);
}
