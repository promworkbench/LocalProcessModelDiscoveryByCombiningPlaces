package org.processmining.placebasedlpmdiscovery.service.lpms;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.datalisteners.DataListener;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableDataType;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.LPMSetDiscoveredEmittableData;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;

public class DefaultLPMSetService implements LPMSetService, DataListener {

    private LPMDiscoveryResult originalLPMSet;

    @Inject
    public DefaultLPMSetService(LPMDiscoveryResult originalResult, DataCommunicationController dc) {
        this.originalLPMSet = originalResult;
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
