package org.processmining.placebasedlpmdiscovery.placechooser;

import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import org.processmining.mockobjects.MockLEFRMatrix;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;

import java.util.*;

public class PlaceChooserGetDefaultTest {

    private static PlaceChooser chooser(XLogWrapper log) {
        return PlaceChooser.getDefault(log);
    }

    @Test
    public void givenMatchingPlace_whenChoose_thenIncluded() {
        // given: log a->b->c; place "a | b" matches log activities and follow relations
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c")));

        // when
        Set<Place> result = chooser(log).choose(new HashSet<>(Collections.singletonList(Place.from("a | b"))), 10);

        // then
        assert result.contains(Place.from("a | b"));
    }

    @Test
    public void givenCountLimit_whenChoose_thenRespected() {
        // given: 3 valid places, count = 2
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c")));
        Set<Place> places = new HashSet<>(Arrays.asList(
                Place.from("a | b"),
                Place.from("a | c"),
                Place.from("b | c")));

        // when
        Set<Place> result = chooser(log).choose(places, 2);

        // then
        assert result.size() == 2;
    }

    @Test
    public void givenPlaceWithActivityNotInLog_whenChoose_thenActivityStripped() {
        // given: x does not appear in the log — IncludedActivitiesTransformer derives its activity
        // set from the log, so x is stripped; resulting place "a | b" still passes all filters
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c")));

        // when
        Set<Place> result = chooser(log).choose(
                new HashSet<>(Collections.singletonList(Place.from("a, x | b"))), 10);

        // then
        assert result.contains(Place.from("a | b"));
    }

    @Test
    public void givenPlaceTransitionsNotInFollowRelations_whenChoose_thenFiltered() {
        // given: d appears only at the start of every trace — it is in the log's activities
        // (so it survives IncludedActivitiesTransformer) but is never a "to" in any follow-relation
        // pair; PassageUsagePlaceTransformer removes the output d → place becomes "a |" →
        // NonEmptyIOTransitionSetPlacePredicate filters it out
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("d", "a", "b", "c")));

        // when
        Set<Place> result = chooser(log).choose(
                new HashSet<>(Collections.singletonList(Place.from("a | d"))), 10);

        // then
        assert result.isEmpty();
    }

    @Test
    public void givenSelfLoopPlace_whenChoose_thenFiltered() {
        // given: log b->a->c → passages include (b,a) and (a,c), so "a" appears as both "from" and "to";
        // place "a | a" survives both transformers but fails NonSelfLoopPlacePredicate
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("b", "a", "c")));

        // when
        Set<Place> result = chooser(log).choose(
                new HashSet<>(Collections.singletonList(Place.from("a | a"))), 10);

        // then
        assert result.isEmpty();
    }

    @Test
    public void givenPlaceWithTooManyArcs_whenChoose_thenFiltered() {
        // given: log a->b->c->d->e->f->g — all transition labels survive both transformers;
        // place has 6 arcs (3 input + 3 output), which exceeds the hard-coded limit of 5
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c", "d", "e", "f", "g")));

        // when
        Set<Place> result = chooser(log).choose(
                new HashSet<>(Collections.singletonList(Place.from("a, b, c | d, e, f"))), 10);

        // then
        assert result.isEmpty();
    }

    @Test
    public void givenDuplicatePlacesAfterTransformation_whenChoose_thenCountRespected() {
        // given: "a, x | b" and "a, y | b" both collapse to "a | b" after IncludedActivitiesTransformer
        // strips x and y (neither appears in the log); with count=2, without distinct() these
        // duplicates consume both slots and the set ends up with only 1 place
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c")));
        Set<Place> places = new HashSet<>(Arrays.asList(
                Place.from("a, x | b"),  // collapses to "a | b"
                Place.from("a, y | b"),  // collapses to "a | b"
                Place.from("b | c")));   // distinct valid place

        // when
        Set<Place> result = chooser(log).choose(places, 2);

        // then: duplicates are deduplicated before the limit, so both distinct places are returned
        assert result.size() == 2;
        assert result.contains(Place.from("a | b"));
        assert result.contains(Place.from("b | c"));
    }

    // --- getDefault(chosenActivities, followRelations, arcsLimit) ---

    private static Map<Pair<String, String>, Integer> knownPairs() {
        Map<Pair<String, String>, Integer> values = new HashMap<>();
        values.put(new Pair<>("a", "c"), 3);
        values.put(new Pair<>("a", "d"), 2);
        values.put(new Pair<>("b", "c"), 1);
        values.put(new Pair<>("b", "d"), 4);
        return values;
    }

    @Test
    public void givenArcsLimitBelowPlaceSize_whenChoose_thenFiltered() {
        // given: arcsLimit=3; place "a, b | c, d" has 4 arcs → filtered
        Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        PlaceChooser chooser = PlaceChooser.getDefault(activities, MockLEFRMatrix.returning(knownPairs()), 3);

        // when
        Set<Place> result = chooser.choose(
                new HashSet<>(Collections.singletonList(Place.from("a, b | c, d"))), 10);

        // then
        assert result.isEmpty();
    }

    @Test
    public void givenArcsLimitAbovePlaceSize_whenChoose_thenIncluded() {
        // given: arcsLimit=4; place "a, b | c, d" has exactly 4 arcs → passes
        Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        PlaceChooser chooser = PlaceChooser.getDefault(activities, MockLEFRMatrix.returning(knownPairs()), 4);

        // when
        Set<Place> result = chooser.choose(
                new HashSet<>(Collections.singletonList(Place.from("a, b | c, d"))), 10);

        // then
        assert result.size() == 1;
        assert result.contains(Place.from("a, b | c, d"));
    }

    @Test
    public void givenFollowRelationsNotIncludingPlacePassages_whenChoose_thenFiltered() {
        // given: only (a,b) is a follow relation — "to" = {b};
        // output c is not in {b} → stripped by PassageUsagePlaceTransformer → empty output → filtered
        Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c"));
        Map<Pair<String, String>, Integer> usedPassages = new HashMap<>();
        usedPassages.put(new Pair<>("a", "b"), 3);
        PlaceChooser chooser = PlaceChooser.getDefault(activities, MockLEFRMatrix.returning(usedPassages), 5);

        // when
        Set<Place> result = chooser.choose(
                new HashSet<>(Collections.singletonList(Place.from("a | c"))), 10);

        // then
        assert result.isEmpty();
    }

    @Test
    public void givenPlaceWithActivityNotInChosenActivities_whenChoose_thenActivityStrippedAndTransformedPlaceReturned() {
        // given: x is not in chosenActivities — stripped by IncludedActivitiesTransformer;
        // the remaining place "a | b" passes all filters and is what gets returned
        Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        PlaceChooser chooser = PlaceChooser.getDefault(activities, MockLEFRMatrix.returning(1), 5);

        // when
        Set<Place> result = chooser.choose(
                new HashSet<>(Collections.singletonList(Place.from("a, x | b"))), 10);

        // then: the original place is not in the result — the transformed one is
        assert result.size() == 1;
        assert result.contains(Place.from("a | b"));
        assert !result.contains(Place.from("a, x | b"));
    }

    @Test
    public void givenActivityAbsentFromFollowRelations_whenChoose_thenTransitionStrippedByPassageTransformer() {
        // given: d is in chosenActivities (survives IncludedActivitiesTransformer) but appears
        // in no follow-relation pair with none of the activities on the other side of the place
        // PassageUsagePlaceTransformer strips it → "a, d | b" becomes "a | b" → passes all filters
        Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c", "d"));
        Map<Pair<String, String>, Integer> usedPassages = new HashMap<>();
        usedPassages.put(new Pair<>("a", "b"), 3);
        usedPassages.put(new Pair<>("a", "c"), 3);
        usedPassages.put(new Pair<>("b", "c"), 3);
        PlaceChooser chooser = PlaceChooser.getDefault(activities, MockLEFRMatrix.returning(usedPassages), 5);

        // when
        Set<Place> result = chooser.choose(
                new HashSet<>(Collections.singletonList(Place.from("a, d | b"))), 10);

        // then: d was stripped by the passage transformer (not the activity transformer);
        // the result contains the twice-transformed place "a | b"
        assert result.size() == 1;
        assert result.contains(Place.from("a | b"));
        assert !result.contains(Place.from("a, d | b"));
    }

    @Test
    public void givenTwoPlacesCollapsingToSameAfterActivityStrip_whenChoose_thenOnlyOneReturned() {
        // given: x and y are not in chosenActivities; "a, x | b" and "a, y | b" both become "a | b"
        // after IncludedActivitiesTransformer — the count is high enough that it cannot be the reason
        // only one place is returned; deduplication is the only explanation
        Set<String> activities = new HashSet<>(Arrays.asList("a", "b", "c"));
        Map<Pair<String, String>, Integer> usedPassages = new HashMap<>();
        usedPassages.put(new Pair<>("a", "b"), 3);
        usedPassages.put(new Pair<>("a", "c"), 3);
        usedPassages.put(new Pair<>("b", "c"), 3);
        PlaceChooser chooser = PlaceChooser.getDefault(activities, MockLEFRMatrix.returning(usedPassages), 5);
        Set<Place> places = new HashSet<>(Arrays.asList(
                Place.from("a, x | b"),
                Place.from("a, y | b")));

        // when
        Set<Place> result = chooser.choose(places, 10);

        // then
        assert result.size() == 1;
        assert result.contains(Place.from("a | b"));
    }
}
