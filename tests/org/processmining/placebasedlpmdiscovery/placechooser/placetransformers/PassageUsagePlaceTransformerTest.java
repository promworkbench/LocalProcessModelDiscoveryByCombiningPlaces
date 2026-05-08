package org.processmining.placebasedlpmdiscovery.placechooser.placetransformers;

import org.apache.commons.math3.util.Pair;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.processmining.mockobjects.MockLEFRMatrix;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.HashMap;
import java.util.Map;

public class PassageUsagePlaceTransformerTest {

    @Test
    public void givenEmptyPassageUsageSet_whenAdapt_thenAllTransitionsAreRemoved() {
        // given
        Place place = Place.from("a | b");
        PassageUsagePlaceTransformer transformer = new PassageUsagePlaceTransformer(MockLEFRMatrix.returning(0));

        // when
        Place adaptedPlace = transformer.adapt(place);

        // then
        assert adaptedPlace.getInputTransitions().isEmpty();
        assert adaptedPlace.getOutputTransitions().isEmpty();
    }

    @Test
    public void givenNonEmptyPassageUsageSetWithAllTransitions_whenAdapt_thenNothingIsRemoved() {
        // given
        Place place = Place.from("a, b | c, d");
        Map<Pair<String, String>, Integer> passageUsageSet = new HashMap<>();
        passageUsageSet.put(new Pair<>("a", "c"), 1);
        passageUsageSet.put(new Pair<>("b", "d"), 1);
        PassageUsagePlaceTransformer transformer =
                new PassageUsagePlaceTransformer(MockLEFRMatrix.returning(passageUsageSet));

        // when
        Place adaptedPlace = transformer.adapt(place);

        // then
        assert adaptedPlace.getInputTransitions().size() == 2;
        Assertions.assertThat(adaptedPlace.getInputTransitions()).containsExactlyInAnyOrder(
                new Transition("a", false), new Transition("b", false));
        assert adaptedPlace.getOutputTransitions().size() == 2;
        Assertions.assertThat(adaptedPlace.getOutputTransitions()).containsExactlyInAnyOrder(
                new Transition("c", false), new Transition("d", false));
    }

    @Test
    public void givenNonEmptyPassageUsageSetWithOneMissingTransition_whenAdapt_thenOnlyMissingTransitionIsRemoved() {
        // given
        Place place = Place.from("a, b | c, d");
        Map<Pair<String, String>, Integer> passageUsageSet = new HashMap<>();
        passageUsageSet.put(new Pair<>("a", "c"), 1);
        passageUsageSet.put(new Pair<>("b", "c"), 1);
        PassageUsagePlaceTransformer transformer =
                new PassageUsagePlaceTransformer(MockLEFRMatrix.returning(passageUsageSet));

        Place placeBigger = Place.from("a, b, e | c, d, f");
        Map<Pair<String, String>, Integer> passageUsageSetBigger = new HashMap<>();
        passageUsageSetBigger.put(new Pair<>("a", "c"), 1);
        passageUsageSetBigger.put(new Pair<>("b", "c"), 1);
        passageUsageSetBigger.put(new Pair<>("e", "f"), 1);
        PassageUsagePlaceTransformer transformerBigger =
                new PassageUsagePlaceTransformer(MockLEFRMatrix.returning(passageUsageSetBigger));

        // when
        Place adaptedPlace = transformer.adapt(place);
        Place adaptedPlaceBigger = transformerBigger.adapt(placeBigger);

        // then
        assert adaptedPlace.getInputTransitions().size() == 2;
        Assertions.assertThat(adaptedPlace.getInputTransitions()).containsExactlyInAnyOrder(
                new Transition("a", false), new Transition("b", false));
        assert adaptedPlace.getOutputTransitions().size() == 1;
        Assertions.assertThat(adaptedPlace.getOutputTransitions()).containsExactlyInAnyOrder(
                new Transition("c", false));

        assert adaptedPlaceBigger.getInputTransitions().size() == 3;
        Assertions.assertThat(adaptedPlaceBigger.getInputTransitions()).containsExactlyInAnyOrder(
                new Transition("a", false), new Transition("b", false),
                new Transition("e", false));
        assert adaptedPlaceBigger.getOutputTransitions().size() == 2;
        Assertions.assertThat(adaptedPlaceBigger.getOutputTransitions()).containsExactlyInAnyOrder(
                new Transition("c", false), new Transition("f", false));
    }
}
