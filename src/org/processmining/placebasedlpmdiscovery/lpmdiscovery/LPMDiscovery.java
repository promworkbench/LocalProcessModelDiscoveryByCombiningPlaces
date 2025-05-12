package org.processmining.placebasedlpmdiscovery.lpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;

public interface LPMDiscovery {

    static LPMDiscovery getInstance() {
        return placeBased(PlacesProvider.getInstance(), 100);
    }

    static LPMDiscovery treeBased() {return new ProcessTreeBasedLPMDiscovery(); }

    static LPMDiscovery placeBased(PlacesProvider placesProvider) {
        return new PlaceBasedLPMDiscovery(placesProvider);
    }

    static LPMDiscovery placeBased(PlacesProvider placesProvider, int placeLimit) {
        return new PlaceBasedLPMDiscovery(placesProvider, placeLimit);
    }

    static LPMDiscovery placeBased(PlacesProvider placesProvider, int placeLimit, int concurrencyLimit) {
        return new PlaceBasedLPMDiscovery(placesProvider, placeLimit, concurrencyLimit);
    }

    LPMDiscoveryResult from(XLog log);
}
