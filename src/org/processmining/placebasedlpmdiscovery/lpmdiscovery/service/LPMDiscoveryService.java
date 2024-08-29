package org.processmining.placebasedlpmdiscovery.lpmdiscovery.service;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;

public interface LPMDiscoveryService {

    LPMDiscoveryResult runLPMDiscovery(XLog log, PlaceSet placeSet);

}
