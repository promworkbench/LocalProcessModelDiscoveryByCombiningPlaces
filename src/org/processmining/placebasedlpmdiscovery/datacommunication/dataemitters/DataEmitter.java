package org.processmining.placebasedlpmdiscovery.datacommunication.dataemitters;

import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

import java.io.OutputStream;

public interface DataEmitter {
    void emit(EmittableData data);
}
