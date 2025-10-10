package src.org.processmining.placebasedlpmdiscovery.model;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

public class PlaceTest {

    @Test
    public void givenOneInOneOutSingleLetter_whenFrom_thenPlaceWithOneInOneOut() {
        // given
        String placeDescription = "a | b";

        // when
        Place placeActual = Place.from(placeDescription);

        // then
        Assert.assertEquals(1, placeActual.getInputTransitions().size());
        Assert.assertEquals(1, placeActual.getOutputTransitions().size());
        Assertions.assertThat(placeActual.getInputTransitions()).containsExactly(new Transition("a", false));
        Assertions.assertThat(placeActual.getOutputTransitions()).containsExactly(new Transition("b", false));
    }

    @Test
    public void givenOneInTwoOutSingleLetter_whenFrom_thenPlaceWithOneInTwoOut() {
        // given
        String placeDescription = "a | b c";

        // when
        Place placeActual = Place.from(placeDescription);

        // then
        Assert.assertEquals(1, placeActual.getInputTransitions().size());
        Assert.assertEquals(2, placeActual.getOutputTransitions().size());
        Assertions.assertThat(placeActual.getInputTransitions())
                .containsExactly(new Transition("a", false));
        Assertions.assertThat(placeActual.getOutputTransitions())
                .containsExactly(new Transition("b", false), new Transition("c", false));
    }

    @Test
    public void givenTwoInOneOutSingleLetter_whenFrom_thenPlaceWithTwoInOneOut() {
        // given
        String placeDescription = "a b | c";

        // when
        Place placeActual = Place.from(placeDescription);

        // then
        Assert.assertEquals(2, placeActual.getInputTransitions().size());
        Assert.assertEquals(1, placeActual.getOutputTransitions().size());
        Assertions.assertThat(placeActual.getInputTransitions())
                .containsExactly(new Transition("a", false), new Transition("b", false));
        Assertions.assertThat(placeActual.getOutputTransitions())
                .containsExactly(new Transition("c", false));
    }

    @Test
    public void givenOneInNoOutSingleLetter_whenFrom_thenPlaceWithOneInNoOut() {
        // given
        String placeDescriptionEmptySpace = "a | ";
        String placeDescriptionNoEndSpace = "a |";
        String placeDescriptionNoSpace = "a|";

        // when
        Place placeActualEmptySpace = Place.from(placeDescriptionEmptySpace);
        Place placeActualNoEndSpace = Place.from(placeDescriptionNoEndSpace);
        Place placeActualNoSpace = Place.from(placeDescriptionNoSpace);

        // then
        Assert.assertEquals(1, placeActualEmptySpace.getInputTransitions().size());
        Assert.assertEquals(0, placeActualEmptySpace.getOutputTransitions().size());
        Assertions.assertThat(placeActualEmptySpace.getInputTransitions())
                .containsExactly(new Transition("a", false));

        Assert.assertEquals(1, placeActualNoEndSpace.getInputTransitions().size());
        Assert.assertEquals(0, placeActualNoEndSpace.getOutputTransitions().size());
        Assertions.assertThat(placeActualNoEndSpace.getInputTransitions())
                .containsExactly(new Transition("a", false));

        Assert.assertEquals(1, placeActualNoSpace.getInputTransitions().size());
        Assert.assertEquals(0, placeActualNoSpace.getOutputTransitions().size());
        Assertions.assertThat(placeActualNoSpace.getInputTransitions())
                .containsExactly(new Transition("a", false));
    }

    @Test
    public void givenNoInOneOutSingleLetter_whenFrom_thenPlaceWithNoInOneOut() {
        // given
        String placeDescriptionEmptySpace = " | a";
        String placeDescriptionNoEndSpace = "| a";
        String placeDescriptionNoSpace = "|a";

        // when
        Place placeActualEmptySpace = Place.from(placeDescriptionEmptySpace);
        Place placeActualNoEndSpace = Place.from(placeDescriptionNoEndSpace);
        Place placeActualNoSpace = Place.from(placeDescriptionNoSpace);

        // then
        Assert.assertEquals(0, placeActualEmptySpace.getInputTransitions().size());
        Assert.assertEquals(1, placeActualEmptySpace.getOutputTransitions().size());
        Assertions.assertThat(placeActualEmptySpace.getOutputTransitions())
                .containsExactly(new Transition("a", false));

        Assert.assertEquals(0, placeActualNoEndSpace.getInputTransitions().size());
        Assert.assertEquals(1, placeActualNoEndSpace.getOutputTransitions().size());
        Assertions.assertThat(placeActualNoEndSpace.getOutputTransitions())
                .containsExactly(new Transition("a", false));

        Assert.assertEquals(0, placeActualNoSpace.getInputTransitions().size());
        Assert.assertEquals(1, placeActualNoSpace.getOutputTransitions().size());
        Assertions.assertThat(placeActualNoSpace.getOutputTransitions())
                .containsExactly(new Transition("a", false));
    }

    @Test
    public void givenMultipleDelimiters_whenFrom_thenThrowIllegalArgument() {
        // given
        String placeDescription = "a b | c | d";

        try { // when
            Place.from(placeDescription);
        }
        catch (IllegalArgumentException e) { // then
            Assert.assertTrue(e.getMessage().contains("The place string should include a single |."));
        }
    }
}
