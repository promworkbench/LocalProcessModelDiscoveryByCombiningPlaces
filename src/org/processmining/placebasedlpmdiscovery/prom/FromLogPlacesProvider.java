package org.processmining.placebasedlpmdiscovery.prom;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.SPECppPlaceDiscoveryParameters;

import java.util.Set;

public class FromLogPlacesProvider implements PlacesProvider {


    private final XLog log;
    private PlaceDiscoveryAlgorithm<?, ?> algorithm;

    public FromLogPlacesProvider(XLog log) {
        this.log = log;
    }

    public FromLogPlacesProvider(XLog log, PlaceDiscoveryAlgorithm<?, ?> algorithm) {
        this.log = log;
        this.algorithm = algorithm;
    }

    public PlacesProvider est() {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
        EstMinerPlaceDiscoveryParameters parameters = new EstMinerPlaceDiscoveryParameters();
        return new FromLogPlacesProvider(this.log, parameters.getAlgorithm(factory));
    }

    public PlacesProvider specpp() {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
        SPECppPlaceDiscoveryParameters parameters = new SPECppPlaceDiscoveryParameters();
        return new FromLogPlacesProvider(this.log, parameters.getAlgorithm(factory));
    }

    @Override
    public Set<Place> provide() {
        if (algorithm == null) {
            return specpp().provide();
        }
        return algorithm.getPlaces(log).getPlaces();
    }
}
