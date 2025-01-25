package org.processmining.placebasedlpmdiscovery.prom;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithm;

import java.util.Set;

public class DiscoveryPlacesProvider implements PlacesProvider {

    private final PlaceDiscoveryAlgorithm<?, ?> algorithm;

    public DiscoveryPlacesProvider(PlaceDiscoveryAlgorithm<?,?> algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public Set<Place> from(XLog log) {
        return algorithm.getPlaces(log).getPlaces();
    }
}
