package src.org.processmining.placebasedlpmdiscovery.placechooser.placetransformers;

import org.apache.commons.math3.util.Pair;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.PassageUsagePlaceTransformer;

import java.util.HashSet;

public class PassageUsagePlaceTransformerTest {

    @Test
    public void givenEmptyPassageUsageSet_whenAdapt_thenAllTransitionsAreRemoved() {
        // given
        Place place = Place.from("a | b");
        PassageUsagePlaceTransformer transformer = new PassageUsagePlaceTransformer(new HashSet<>());

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
        HashSet<Pair<String, String>> passageUsageSet = new HashSet<>();
        passageUsageSet.add(new Pair<>("a", "c"));
        passageUsageSet.add(new Pair<>("b", "d"));
        PassageUsagePlaceTransformer transformer = new PassageUsagePlaceTransformer(passageUsageSet);

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
        HashSet<Pair<String, String>> passageUsageSet = new HashSet<>();
        passageUsageSet.add(new Pair<>("a", "c"));
        passageUsageSet.add(new Pair<>("b", "c"));
        PassageUsagePlaceTransformer transformer = new PassageUsagePlaceTransformer(passageUsageSet);

        Place placeBigger = Place.from("a, b, e | c, d, f");
        HashSet<Pair<String, String>> passageUsageSetBigger = new HashSet<>();
        passageUsageSetBigger.add(new Pair<>("a", "c"));
        passageUsageSetBigger.add(new Pair<>("b", "c"));
        passageUsageSetBigger.add(new Pair<>("e", "f"));
        PassageUsagePlaceTransformer transformerBigger = new PassageUsagePlaceTransformer(passageUsageSetBigger);

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
