package org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata;

import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;

public class LPMSetDiscoveredEmittableData implements EmittableData {

    private final LPMDiscoveryResult discoveryResult;

    public LPMSetDiscoveredEmittableData(LPMDiscoveryResult discoveryResult) {
        this.discoveryResult = discoveryResult;
    }

    @Override
    public EmittableDataType getType() {
        return EmittableDataType.LPMSetDiscovered;
    }

    public LPMDiscoveryResult getDiscoveryResult() {
        return discoveryResult;
    }
}
