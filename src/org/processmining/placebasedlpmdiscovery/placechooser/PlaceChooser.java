package org.processmining.placebasedlpmdiscovery.placechooser;

import org.apache.commons.math3.util.Pair;
import org.processmining.lpms.discovery.DiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.MostKArcsPlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.NonEmptyIOTransitionSetPlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.NonSelfLoopPlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.PlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.TransitionCountPlaceRankConverter;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.IncludedActivitiesPlaceTransformer;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.PassageUsagePlaceTransformer;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Selects a ranked subset of places from a candidate set.
 *
 * <p>A {@code PlaceChooser} processes each place through an ordered pipeline of
 * {@link org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.PlaceTransformer PlaceTransformer}s
 * and {@link org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.PlacePredicate PlacePredicate}s,
 * ranks the survivors with a
 * {@link org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.PlaceRankConverter PlaceRankConverter},
 * and returns at most {@code count} top-ranked places.
 *
 * <p>Use {@link #builder()} to compose a custom pipeline, or {@link #getDefault(EventLog)} /
 * {@link #getDefault(Set, Set, int)} for the standard configuration.
 */
public interface PlaceChooser {

    /**
     * Creates a default {@code PlaceChooser} configured for the given event log.
     *
     * <p>The default pipeline:
     * <ol>
     *   <li>restricts transitions to activities present in the log (see {@link IncludedActivitiesPlaceTransformer}),
     *   </li>
     *   <li>weights transitions by passage usage (see {@link PassageUsagePlaceTransformer}),</li>
     *   <li>removes self-loop places with no other input and output transitions (see
     *   {@link NonSelfLoopPlacePredicate}),</li>
     *   <li>removes places with empty input or output transition sets (see
     *   {@link NonEmptyIOTransitionSetPlacePredicate}),</li>
     *   <li>removes places with more than 5 arcs (see {@link MostKArcsPlacePredicate}),</li>
     *   <li>ranks by total transition count (see {@link TransitionCountPlaceRankConverter}).</li>
     * </ol>
     *
     * @param eventLog the event log used to derive activities and follow relations
     * @return a fully configured {@code PlaceChooser}
     */
    static PlaceChooser getDefault(EventLog eventLog) {
        return getDefault(
                eventLog.getActivities().stream().map(Activity::getName).collect(Collectors.toSet()),
                LogUtils.getFollowRelations(eventLog.getOriginalLog(), DiscoveryParameters.Default.proximity),
                5);
    }

    /**
     * Creates a default {@code PlaceChooser} with explicit activity and relation parameters.
     *
     * @param chosenActivities the set of activity names that places may reference
     * @param followRelations  directly-follows pairs used to weight passage usage
     * @param arcsLimit        maximum number of arcs a place may have to pass the arc filter
     * @return a fully configured {@code PlaceChooser}
     */
    static PlaceChooser getDefault(Set<String> chosenActivities, Set<Pair<String, String>> followRelations,
                                   int arcsLimit) {
        return PlaceChooser.builder()
                .withTransformer(new IncludedActivitiesPlaceTransformer(chosenActivities))
                .withTransformer(new PassageUsagePlaceTransformer(followRelations))
                .withFilter(PlacePredicate.selfLoop())
                .withFilter(PlacePredicate.emptyIOTransitionSet())
                .withFilter(PlacePredicate.mostKArcs(arcsLimit))
                .rankBy(new TransitionCountPlaceRankConverter())
                .build();
    }

    /** Returns a new {@link PlaceChooserBuilder} for composing a custom chooser pipeline. */
    static PlaceChooserBuilder builder() {
        return new PlaceChooserBuilder();
    }

    /**
     * Chooses up to {@code count} places from the candidate set.
     *
     * <p>Each place is first passed through the pipeline's transformers and filters (in registration order).
     * Surviving places are ranked and the top {@code count} are returned.
     *
     * @param places the full candidate set of places
     * @param count  the maximum number of places to return
     * @return at most {@code count} top-ranked places that passed all filters
     */
    Set<Place> choose(Set<Place> places, int count);
}
