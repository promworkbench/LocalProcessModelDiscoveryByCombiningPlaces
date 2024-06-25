package org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata;

public class LPMGroupingFinished implements EmittableData {
    @Override
    public EmittableDataType getType() {
        return EmittableDataType.LPMGroupingFinished;
    }
}
