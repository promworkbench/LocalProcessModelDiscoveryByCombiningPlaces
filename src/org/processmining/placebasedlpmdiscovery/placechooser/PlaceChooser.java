package org.processmining.placebasedlpmdiscovery.placechooser;

import org.apache.commons.math3.util.Pair;
import org.processmining.lpms.discovery.DiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.MostKArcsPlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.NonEmptyIOTransitionSetPlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.NonSelfLoopPlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.TransitionCountPlaceRankConverter;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.IncludedActivitiesPlaceTransformer;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.PassageUsagePlaceTransformer;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.Set;
import java.util.stream.Collectors;

public interface PlaceChooser {

    static PlaceChooser getDefault(EventLog eventLog) {
        return getDefault(
                eventLog.getActivities().stream().map(Activity::getName).collect(Collectors.toSet()),
                LogUtils.getFollowRelations(eventLog.getOriginalLog(), DiscoveryParameters.Default.proximity),
                5);
    }

    static PlaceChooser getDefault(Set<String> chosenActivities, Set<Pair<String, String>> followRelations,
                                   int arcsLimit) {
        return PlaceChooser.builder()
                .withTransformer(new IncludedActivitiesPlaceTransformer(chosenActivities))
                .withTransformer(new PassageUsagePlaceTransformer(followRelations))
                .withFilter(new NonSelfLoopPlacePredicate())
                .withFilter(new NonEmptyIOTransitionSetPlacePredicate())
                .withFilter(new MostKArcsPlacePredicate(arcsLimit))
                .withRankConverter(new TransitionCountPlaceRankConverter())
                .build();
    }

    static PlaceChooserBuilder builder() {
        return new PlaceChooserBuilder();
    }

    Set<Place> choose(Set<Place> places, int count);
}
