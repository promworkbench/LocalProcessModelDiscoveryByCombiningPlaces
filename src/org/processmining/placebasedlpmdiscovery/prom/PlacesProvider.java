package org.processmining.placebasedlpmdiscovery.prom;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.NonSelfLoopPlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.RankedPlace;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.RankedPlaceComparator;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.TransitionCountPlaceRankConverter;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.IncludedActivitiesPlaceTransformer;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.PassageUsagePlaceTransformer;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.Set;
import java.util.stream.Collectors;

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
     * Creates a PlacesProvider that provides the top K ranked places from the base provider.
     * @param baseProvider the base PlacesProvider to get places from
     * @param k the number of top ranked places to provide
     * @return a PlacesProvider that provides the top K ranked places
     */
    static PlacesProvider topK(PlacesProvider baseProvider, int k) {
        return () -> baseProvider.provide().stream()
                .map(p -> new RankedPlace(p,
                        new TransitionCountPlaceRankConverter().convert(p)
                        /*, new TotalPassageCoveragePlaceRankConverter(lefr).convert(p) */))
                .sorted(new RankedPlaceComparator())
                .map(RankedPlace::getPlace)
                .limit(k)
                .collect(Collectors.toSet());
    }

    static PlacesProvider noSelfLoopPlaces(PlacesProvider baseProvider) {
        return () -> baseProvider.provide().stream()
                .filter(new NonSelfLoopPlacePredicate())
                .collect(Collectors.toSet());
    }

    static PlacesProvider noEmptyIOPlaces(PlacesProvider baseProvider) {
        return () -> baseProvider.provide().stream()
                .filter(new NonSelfLoopPlacePredicate())
                .collect(Collectors.toSet());
    }

    static PlacesProvider onlyForActivities(PlacesProvider baseProvider, Set<String> activities) {
        return () -> baseProvider.provide().stream()
                .map(new IncludedActivitiesPlaceTransformer(activities))
                .collect(Collectors.toSet());
    }

    static PlacesProvider onlyOccurringInProximity(PlacesProvider baseProvider, int proximity, XLog log) {
        return () -> baseProvider.provide().stream()
                .map(new PassageUsagePlaceTransformer(LogUtils.getFollowRelations(log, proximity)))
                .collect(Collectors.toSet());
    }

    /**
     * Provides a set of places.
     * @return a set of places
     */
    Set<Place> provide();
}
