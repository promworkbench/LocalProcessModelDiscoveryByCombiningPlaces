package org.processmining.placebasedlpmdiscovery.prom;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.SPECppPlaceDiscoveryParameters;

import java.util.Set;

public interface PlacesProvider {

    static PlacesProvider getInstance() {
        return specpp();
    }

    static PlacesProvider est() {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
        EstMinerPlaceDiscoveryParameters parameters = new EstMinerPlaceDiscoveryParameters();
        return new DiscoveryPlacesProvider(parameters.getAlgorithm(factory));
    }

    static PlacesProvider specpp() {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
        SPECppPlaceDiscoveryParameters parameters = new SPECppPlaceDiscoveryParameters();
        return new DiscoveryPlacesProvider(parameters.getAlgorithm(factory));
    }

    Set<Place> from(XLog log);
}
