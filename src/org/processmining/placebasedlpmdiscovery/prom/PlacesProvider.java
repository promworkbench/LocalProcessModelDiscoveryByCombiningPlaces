package org.processmining.placebasedlpmdiscovery.prom;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Set;

/**
 * A provider of places for LPM discovery. Implementations can provide places from various sources,
 * such as files, sets, or by discovering them from an event log.
 */
public interface PlacesProvider {

    /**
     * Creates a PlacesProvider that reads places from a file.
     * @param fileName the name of the file to read places from
     * @return a PlacesProvider that reads places from the specified file
     */
    static PlacesProvider fromFile(String fileName) {
        return new FromFilePlacesProvider(fileName);
    }

    /**
     * Creates a PlacesProvider that provides the given set of places.
     * @param places the set of places to provide
     * @return a PlacesProvider that provides the given set of places
     */
    static PlacesProvider fromSet(Set<Place> places) {
        return () -> places;
    }

    /**
     * Creates a PlacesProvider that provides the given set of places by discovering them in the event log.
     * @param log the XLog to discover places from
     * @return a PlacesProvider that discovers places from the given log
     */
    static PlacesProvider fromLog(XLog log) {
        return FromLogPlacesProvider.recommended(log);
    }

    /**
     * Provides a set of places.
     * @return a set of places
     */
    Set<Place> provide();
}
