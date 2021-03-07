package org.processmining.placebasedlpmdiscovery.placediscovery.algorithms;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.placediscovery.PlaceDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.placediscovery.converters.place.AbstractPlaceConverter;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.PlaceDiscoveryParameters;

/**
 * Given a log it discovers a set of places for that log
 *
 * @param <P> The parameters needed for the place discovery
 * @param <R> The result of the place discovery algorithm
 */
public abstract class PlaceDiscoveryAlgorithm
        <P extends PlaceDiscoveryParameters, R> {

    protected AbstractPlaceConverter<R> converter;
    protected P parameters;

    public PlaceDiscoveryAlgorithm(AbstractPlaceConverter<R> converter, P parameters) {
        this.converter = converter;
        this.parameters = parameters;
    }

    /**
     * Extracts a set of places for a given event log
     *
     * @param log: the event log for which the set of places is extracted
     * @return the set of places extracted from the event log and possibly additional information
     */
    public abstract PlaceDiscoveryResult getPlaces(XLog log);
}
