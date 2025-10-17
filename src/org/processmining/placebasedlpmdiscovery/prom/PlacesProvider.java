package org.processmining.placebasedlpmdiscovery.prom;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Set;

public interface PlacesProvider {

    static PlacesProvider fromFile(String fileName) {
        return new FromFilePlacesProvider(fileName);
    }

    static PlacesProvider fromSet(Set<Place> places) {
        return () -> places;
    }

    static FromLogPlacesProvider.FromLogPlacesProviderLogStage fromLog(XLog log) {
        return new FromLogPlacesProvider.FromLogPlacesProviderLogStage(log);
    }

    Set<Place> provide();
}
