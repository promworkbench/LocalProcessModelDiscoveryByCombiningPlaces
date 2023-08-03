package org.processmining.placebasedlpmdiscovery.prom.placediscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.PlaceDiscoveryParameters;

public class StandardPlaceDiscovery implements PlaceDiscovery {

    private final XLog log;

    private final PlaceDiscoveryParameters parameters;

    public StandardPlaceDiscovery(XLog log, PlaceDiscoveryParameters parameters){
        this.log = log;
        this.parameters = parameters;
    }
    public PlaceDiscoveryResult getPlaces() {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory(); // TODO: Why not using the factory directly?
        PlaceDiscoveryAlgorithm<? extends PlaceDiscoveryParameters, ?> algorithm = parameters.getAlgorithm(factory);
        return algorithm.getPlaces(log);
    }
}
