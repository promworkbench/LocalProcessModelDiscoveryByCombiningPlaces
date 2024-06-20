package org.processmining.placebasedlpmdiscovery.service.lpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

public interface LPMDiscoveryService {

    void runLPMDiscovery(XLog log, PlaceSet placeSet);

}
