package org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata;

public class LPMOccurredEmittableData implements EmittableData {
    @Override
    public EmittableDataType getType() {
        return EmittableDataType.LPMOccurred;
    }
}
