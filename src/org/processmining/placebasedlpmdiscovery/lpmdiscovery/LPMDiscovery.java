package org.processmining.placebasedlpmdiscovery.lpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;

public interface LPMDiscovery {

    static LPMDiscovery getInstance() {
        return placeBased(PlacesProvider.getInstance(), 50);
    }

    static LPMDiscovery placeBased(PlacesProvider placesProvider, int placeLimit) {
        return new PlaceBasedLPMDiscovery(placesProvider, placeLimit);
    }

    LPMDiscoveryResult from(XLog log);
}
