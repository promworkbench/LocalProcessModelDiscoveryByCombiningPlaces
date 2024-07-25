package org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata;

public class LPMGroupingFinished implements EmittableData {
    private final String identifier;

    public LPMGroupingFinished(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public EmittableDataType getType() {
        return EmittableDataType.LPMGroupingFinished;
    }

    public String getIdentifier() {
        return identifier;
    }
}
