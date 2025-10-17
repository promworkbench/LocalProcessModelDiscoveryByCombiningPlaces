package org.processmining.placebasedlpmdiscovery.prom;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Set;

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
     * Creates a FromLogPlacesProviderLogStage to configure place discovery from an XLog.
     * @param log the XLog to discover places from
     * @return a FromLogPlacesProviderLogStage to configure place discovery
     */
    static FromLogPlacesProvider.FromLogPlacesProviderVariantProvider fromLog(XLog log) {
        return new FromLogPlacesProvider.FromLogPlacesProviderVariantProvider(log);
    }

    Set<Place> provide();
}
