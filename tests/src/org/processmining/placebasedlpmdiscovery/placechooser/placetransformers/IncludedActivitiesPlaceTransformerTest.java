package src.org.processmining.placebasedlpmdiscovery.placechooser.placetransformers;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.IncludedActivitiesPlaceTransformer;

import java.util.HashSet;
import java.util.Set;

public class IncludedActivitiesPlaceTransformerTest {

    @Test
    public void givenEmptyActivitiesSet_whenAdapt_thenAllNonInvisibleTransitionsAreRemoved() {
        // given
        Place place = Place.from("a | b");
        Place placeWithInvisibleTransitions = Place.from("a | b");
        placeWithInvisibleTransitions.addInputTransition(new Transition("tau", true));
        placeWithInvisibleTransitions.addOutputTransition(new Transition("tau", true));
        IncludedActivitiesPlaceTransformer transformer = new IncludedActivitiesPlaceTransformer(new HashSet<>());

        // when
        Place transformedPlace = transformer.adapt(place);
        Place transformedPlaceWithInvisible = transformer.adapt(placeWithInvisibleTransitions);

        // then
        assert transformedPlace.getInputTransitions().isEmpty();
        assert transformedPlace.getOutputTransitions().isEmpty();

        assert transformedPlaceWithInvisible.getInputTransitions().size() == 1;
        Assertions.assertThat(transformedPlaceWithInvisible.getInputTransitions())
                .containsExactly(new Transition("tau", true));
        assert transformedPlaceWithInvisible.getOutputTransitions().size() == 1;
        Assertions.assertThat(transformedPlaceWithInvisible.getOutputTransitions())
                .containsExactly(new Transition("tau", true));
    }

    @Test
    public void givenNonEmptyActivitiesSet_whenAdapt_thenOnlyNonIncludedNonInvisibleTransitionsAreRemoved() {
        // given
        Place place = Place.from("a | b, c");
        Place placeWithInvisibleTransitions = Place.from("a | b, c");
        placeWithInvisibleTransitions.addOutputTransition(new Transition("tau", true));
        Set<String> activities = new HashSet<>();
        activities.add("a");
        IncludedActivitiesPlaceTransformer transformer = new IncludedActivitiesPlaceTransformer(activities);

        // when
        Place transformedPlace = transformer.adapt(place);
        Place transformedPlaceWithInvisible = transformer.adapt(placeWithInvisibleTransitions);

        // then
        assert transformedPlace.getInputTransitions().size() == 1;
        Assertions.assertThat(transformedPlace.getInputTransitions())
                .containsExactly(new Transition("a", false));
        assert transformedPlace.getOutputTransitions().isEmpty();

        assert transformedPlaceWithInvisible.getInputTransitions().size() == 1;
        Assertions.assertThat(transformedPlaceWithInvisible.getInputTransitions())
                .containsExactly(new Transition("a", false));
        assert transformedPlaceWithInvisible.getOutputTransitions().size() == 1;
        Assertions.assertThat(transformedPlaceWithInvisible.getOutputTransitions())
                .containsExactly(new Transition("tau", true));
    }

    @Test
    public void givenAllActivitiesIncluded_whenAdapt_thenNoTransitionsAreRemoved() {
        // given
        Place place = Place.from("a | b");
        Place placeWithInvisibleTransitions = Place.from("a | b");
        placeWithInvisibleTransitions.addInputTransition(new Transition("tau", true));
        placeWithInvisibleTransitions.addOutputTransition(new Transition("tau", true));
        Set<String> activities = new HashSet<>();
        activities.add("a");
        activities.add("b");
        IncludedActivitiesPlaceTransformer transformer = new IncludedActivitiesPlaceTransformer(activities);

        // when
        Place transformedPlace = transformer.adapt(place);
        Place transformedPlaceWithInvisible = transformer.adapt(placeWithInvisibleTransitions);

        // then
        assert transformedPlace.getInputTransitions().size() == 1;
        Assertions.assertThat(transformedPlace.getInputTransitions())
                .containsExactly(new Transition("a", false));
        assert transformedPlace.getOutputTransitions().size() == 1;
        Assertions.assertThat(transformedPlace.getOutputTransitions())
                .containsExactly(new Transition("b", false));

        assert transformedPlaceWithInvisible.getInputTransitions().size() == 2;
        Assertions.assertThat(transformedPlaceWithInvisible.getInputTransitions()).containsExactlyInAnyOrder(
                new Transition("a", false), new Transition("tau", true));
        assert transformedPlaceWithInvisible.getOutputTransitions().size() == 2;
        Assertions.assertThat(transformedPlaceWithInvisible.getOutputTransitions()).containsExactlyInAnyOrder(
                new Transition("b", false), new Transition("tau", true));
    }

    @Test
    public void givenNoTransitions_whenAdapt_thenPlaceRemainsUnchanged() {
        // given
        Place place = Place.from(" | ");
        Set<String> activities = new HashSet<>();
        IncludedActivitiesPlaceTransformer transformer = new IncludedActivitiesPlaceTransformer(activities);

        // when
        Place transformedPlace = transformer.adapt(place);

        // then
        assert transformedPlace.getInputTransitions().isEmpty();
        assert transformedPlace.getOutputTransitions().isEmpty();
    }

    @Test
    public void givenOnlyInvisibleTransitions_whenAdapt_thenPlaceRemainsUnchanged() {
        // given
        Place place = Place.from(" | ");
        place.addInputTransition(new Transition("tau1", true));
        place.addOutputTransition(new Transition("tau2", true));
        Set<String> activities = new HashSet<>();
        IncludedActivitiesPlaceTransformer transformer = new IncludedActivitiesPlaceTransformer(activities);

        // when
        Place transformedPlace = transformer.adapt(place);

        // then
        assert transformedPlace.getInputTransitions().size() == 1;
        Assertions.assertThat(transformedPlace.getInputTransitions())
                .containsExactly(new Transition("tau1", true));
        assert transformedPlace.getOutputTransitions().size() == 1;
        Assertions.assertThat(transformedPlace.getOutputTransitions())
                .containsExactly(new Transition("tau2", true));
    }

}
