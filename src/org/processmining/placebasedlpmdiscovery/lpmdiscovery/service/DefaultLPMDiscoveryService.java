package org.processmining.placebasedlpmdiscovery.lpmdiscovery.service;

import com.google.inject.Inject;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.LPMSetDiscoveredEmittableData;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public class DefaultLPMDiscoveryService implements LPMDiscoveryService {

    private final DataCommunicationController dc;

    @Inject
    public DefaultLPMDiscoveryService(DataCommunicationController dc) {
        this.dc = dc;
    }

    @Override
    public LPMDiscoveryResult runLPMDiscovery(XLog log, PlaceSet placeSet) {
        LPMDiscoveryResult result = Main.createDefaultBuilder(log, placeSet,
                new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log))).build().run();
        dc.emit(new LPMSetDiscoveredEmittableData(result));
        return result;
    }
}
