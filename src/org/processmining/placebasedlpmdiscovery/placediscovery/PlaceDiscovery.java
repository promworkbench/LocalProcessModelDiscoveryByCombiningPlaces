package org.processmining.placebasedlpmdiscovery.placediscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithm;
import org.processmining.placebasedlpmdiscovery.placediscovery.algorithms.PlaceDiscoveryAlgorithmFactory;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.PlaceDiscoveryParameters;

/**
 * Class that takes care of the place discovery part of the system.
 */
public class PlaceDiscovery {
    /**
     * Discovers places for the given event log using the discovery algorithm sent as second parameter
     *
     * @param log: the event log for which places are discovered
     * @return set of places
     */
    public static PlaceDiscoveryResult discover(XLog log, PlaceDiscoveryParameters parameters) {
        PlaceDiscoveryAlgorithmFactory factory = new PlaceDiscoveryAlgorithmFactory();
        PlaceDiscoveryAlgorithm<? extends PlaceDiscoveryParameters, ?> algorithm = parameters.getAlgorithm(factory);
        PlaceDiscoveryResult result = algorithm.getPlaces(log);
        int allPlacesCount = result.getPlaces().size();
        result.setPlaces(result.getPlaces());
        if (result.getLog() == null)
            result.setLog(log);
        int filteredPlacesCount = result.getPlaces().size();
        return result;
    }
}
