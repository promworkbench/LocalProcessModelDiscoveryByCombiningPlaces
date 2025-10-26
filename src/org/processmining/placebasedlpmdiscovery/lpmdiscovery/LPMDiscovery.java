package org.processmining.placebasedlpmdiscovery.lpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.lpms.discovery.lada.LADA;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;

public interface LPMDiscovery {

    static LPMDiscovery getInstance() {
        return placeBased();
    }

    static LPMDiscovery treeBased() { return new ProcessTreeBasedLPMDiscovery(); }

    static LPMDiscovery placeBased() {
        return new PlaceBasedLPMDiscovery(PlacesProvider::fromLog);
    }

    static LPMDiscovery placeBased(PlacesProvider placesProvider) {
        return new PlaceBasedLPMDiscovery((XLog log) -> placesProvider);
    }

    static LPMDiscovery placeBased(PlacesProvider placesProvider, int placeLimit) {
        return new PlaceBasedLPMDiscovery((XLog log) -> placesProvider, placeLimit);
    }

    static LPMDiscovery placeBased(PlacesProvider placesProvider, int placeLimit, int concurrencyLimit) {
        return new PlaceBasedLPMDiscovery((XLog log) -> placesProvider, placeLimit, concurrencyLimit);
    }

    static LPMDiscovery ladaBased(int proximityLimit) {
        return new LADA(proximityLimit);
    }

    LPMDiscoveryResult from(XLog log);
}
