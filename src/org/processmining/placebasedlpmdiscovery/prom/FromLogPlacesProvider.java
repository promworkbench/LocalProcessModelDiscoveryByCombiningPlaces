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
    private final PlaceDiscoveryAlgorithm<?, ?> algorithm;

    public FromLogPlacesProvider(XLog log, PlaceDiscoveryAlgorithm<?, ?> algorithm) {
        this.log = log;
        this.algorithm = algorithm;
    }

    @Override
    public Set<Place> provide() {
        return algorithm.getPlaces(this.log).getPlaces();
    }


    public static class FromLogPlacesProviderVariantProvider {

        private final XLog log;

        FromLogPlacesProviderVariantProvider(XLog log) {
            this.log = log;
        }

        public PlacesProvider recommended() {
            return specpp();
        }

        public PlacesProvider est() {
            PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
            EstMinerPlaceDiscoveryParameters parameters = new EstMinerPlaceDiscoveryParameters();
            return new FromLogPlacesProvider(log, parameters.getAlgorithm(factory));
        }

        public PlacesProvider specpp() {
            PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
            SPECppPlaceDiscoveryParameters parameters = new SPECppPlaceDiscoveryParameters();
            return new FromLogPlacesProvider(log, parameters.getAlgorithm(factory));
        }
    }
}
