package org.processmining.placebasedlpmdiscovery.service.lpms;

import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.datalisteners.DataListener;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableDataType;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.LPMSetDiscoveredEmittableData;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;

public class DeafaultLPMSetService implements LPMSetService, DataListener {

    private LPMDiscoveryResult originalLPMSet;

    public DeafaultLPMSetService(DataCommunicationController dc) {
        dc.registerDataListener(this, EmittableDataType.LPMSetDiscovered);
    }

    @Override
    public LPMDiscoveryResult getOriginalLPMSet() {
        return this.originalLPMSet;
    }

    @Override
    public LPMDiscoveryResult getLPMSet() {
        return this.originalLPMSet;
    }

    @Override
    public void receive(EmittableData data) {
        if (data.getType().equals(EmittableDataType.LPMSetDiscovered)) {
            LPMSetDiscoveredEmittableData cData = (LPMSetDiscoveredEmittableData) data;
            this.originalLPMSet = cData.getDiscoveryResult();
        }
    }
}
