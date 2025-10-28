package org.processmining.placebasedlpmdiscovery.lpmdiscovery.service;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.LPMSetDiscoveredEmittableData;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.LPMDiscovery;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;

public class DefaultLPMDiscoveryService implements LPMDiscoveryService {

    private final DataCommunicationController dc;

    @Inject
    public DefaultLPMDiscoveryService(DataCommunicationController dc) {
        this.dc = dc;
    }

    @Override
    public LPMDiscoveryResult runLPMDiscovery(EventLog log, PlaceSet placeSet) {
        LPMDiscoveryResult result = LPMDiscovery.placeBased(PlacesProvider.fromSet(placeSet.getPlaces().getPlaces()))
                .from(log.getOriginalLog());
        dc.emit(new LPMSetDiscoveredEmittableData(result));
        return result;
    }
}
