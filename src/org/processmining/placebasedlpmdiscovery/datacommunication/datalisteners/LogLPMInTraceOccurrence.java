package org.processmining.placebasedlpmdiscovery.datacommunication.datalisteners;

import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.LPMOccurredEmittableData;

import java.io.OutputStream;

public class LogLPMInTraceOccurrence implements DataListener {

    private final OutputStream os;

    public LogLPMInTraceOccurrence(OutputStream os) {
        this.os = os;
    }
    @Override
    public void receive(EmittableData data) {
        if (!(data instanceof LPMOccurredEmittableData)) {
            throw new IllegalArgumentException("The emittable data received is of type " +
                    data.getClass() + " and the expected type is " + LPMOccurredEmittableData.class);
        }
        LPMOccurredEmittableData cData = (LPMOccurredEmittableData) data;
        // TODO: send it in the stream
    }
}
