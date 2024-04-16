package org.processmining.placebasedlpmdiscovery.datacommunication.datalisteners;

import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;

public interface DataListener {

    void receive(EmittableData data);
}
