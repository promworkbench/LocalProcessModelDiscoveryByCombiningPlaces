package org.processmining.placebasedlpmdiscovery.model;

import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

public class PlaceTest {

    @Test
    public void givenOneInOneOutSingleLetter_whenFrom_thenPlaceWithOneInOneOut() {
        // given
        String placeDescription = "a | b";
        String placeDescriptionWithComma = "a, | b";
        String placeDescriptionWithEnter = "a\n, | b";

        // when
        Place placeActual = Place.from(placeDescription);
        Place placeActualWithComma = Place.from(placeDescriptionWithComma);
        Place placeActualWithEnter = Place.from(placeDescriptionWithEnter);

        // then
        Assert.assertEquals(1, placeActual.getInputTransitions().size());
        Assert.assertEquals(1, placeActual.getOutputTransitions().size());
        Assertions.assertThat(placeActual.getInputTransitions())
                .containsExactly(new Transition("a", false));
        Assertions.assertThat(placeActual.getOutputTransitions())
                .containsExactly(new Transition("b", false));

        Assert.assertEquals(1, placeActualWithComma.getInputTransitions().size());
        Assert.assertEquals(1, placeActualWithComma.getOutputTransitions().size());
        Assertions.assertThat(placeActualWithComma.getInputTransitions())
                .containsExactly(new Transition("a", false));
        Assertions.assertThat(placeActualWithComma.getOutputTransitions())
                .containsExactly(new Transition("b", false));

        Assert.assertEquals(1, placeActualWithEnter.getInputTransitions().size());
        Assert.assertEquals(1, placeActualWithEnter.getOutputTransitions().size());
        Assertions.assertThat(placeActualWithEnter.getInputTransitions())
                .containsExactly(new Transition("a", false));
        Assertions.assertThat(placeActualWithEnter.getOutputTransitions())
                .containsExactly(new Transition("b", false));
    }

    @Test
    public void givenOneInOneOutWords_whenFrom_thenPlaceWithOneInOneOut() {
        // given
        String placeDescription = "apple | banana";
        String placeDescriptionWithComma = "apple, | banana";

        // when
        Place placeActual = Place.from(placeDescription);
        Place placeActualWithComma = Place.from(placeDescriptionWithComma);

        // then
        Assert.assertEquals(1, placeActual.getInputTransitions().size());
        Assert.assertEquals(1, placeActual.getOutputTransitions().size());
        Assertions.assertThat(placeActual.getInputTransitions()).containsExactly(new Transition("apple", false));
        Assertions.assertThat(placeActual.getOutputTransitions()).containsExactly(new Transition("banana", false));

        Assert.assertEquals(1, placeActualWithComma.getInputTransitions().size());
        Assert.assertEquals(1, placeActualWithComma.getOutputTransitions().size());
        Assertions.assertThat(placeActualWithComma.getInputTransitions()).containsExactly(new Transition("apple", false));
        Assertions.assertThat(placeActualWithComma.getOutputTransitions()).containsExactly(new Transition("banana", false));
    }

    @Test
    public void givenOneInOneOutPhrases_whenFrom_thenPlaceWithOneInOneOut() {
        // given
        String placeDescription = "tasty apple | tasty banana";
        String placeDescriptionWithComma = "tasty apple, | tasty banana";

        // when
        Place placeActual = Place.from(placeDescription);
        Place placeActualWithComma = Place.from(placeDescriptionWithComma);

        // then
        Assert.assertEquals(1, placeActual.getInputTransitions().size());
        Assert.assertEquals(1, placeActual.getOutputTransitions().size());
        Assertions.assertThat(placeActual.getInputTransitions())
                .containsExactly(new Transition("tasty apple", false));
        Assertions.assertThat(placeActual.getOutputTransitions())
                .containsExactly(new Transition("tasty banana", false));

        Assert.assertEquals(1, placeActualWithComma.getInputTransitions().size());
        Assert.assertEquals(1, placeActualWithComma.getOutputTransitions().size());
        Assertions.assertThat(placeActualWithComma.getInputTransitions())
                .containsExactly(new Transition("tasty apple", false));
        Assertions.assertThat(placeActualWithComma.getOutputTransitions())
                .containsExactly(new Transition("tasty banana", false));
    }

    @Test
    public void givenOneInTwoOutSingleLetter_whenFrom_thenPlaceWithOneInTwoOut() {
        // given
        String placeDescription = "a | b, c";

        // when
        Place placeActual = Place.from(placeDescription);

        // then
        Assert.assertEquals(1, placeActual.getInputTransitions().size());
        Assert.assertEquals(2, placeActual.getOutputTransitions().size());
        Assertions.assertThat(placeActual.getInputTransitions())
                .containsExactly(new Transition("a", false));
        Assertions.assertThat(placeActual.getOutputTransitions())
                .containsExactlyInAnyOrder(new Transition("b", false),
                        new Transition("c", false));
    }

    @Test
    public void givenOneInTwoOutWords_whenFrom_thenPlaceWithOneInTwoOut() {
        // given
        String placeDescription = "apple | banana, cherry";

        // when
        Place placeActual = Place.from(placeDescription);

        // then
        Assert.assertEquals(1, placeActual.getInputTransitions().size());
        Assert.assertEquals(2, placeActual.getOutputTransitions().size());
        Assertions.assertThat(placeActual.getInputTransitions())
                .containsExactly(new Transition("apple", false));
        Assertions.assertThat(placeActual.getOutputTransitions())
                .containsExactlyInAnyOrder(new Transition("banana", false),
                        new Transition("cherry", false));
    }

    @Test
    public void givenOneInTwoOutPhrases_whenFrom_thenPlaceWithOneInTwoOut() {
        // given
        String placeDescription = "tasty apple | tasty banana, tasty cherry";

        // when
        Place placeActual = Place.from(placeDescription);

        // then
        Assert.assertEquals(1, placeActual.getInputTransitions().size());
        Assert.assertEquals(2, placeActual.getOutputTransitions().size());
        Assertions.assertThat(placeActual.getInputTransitions())
                .containsExactly(new Transition("tasty apple", false));
        Assertions.assertThat(placeActual.getOutputTransitions())
                .containsExactlyInAnyOrder(new Transition("tasty banana", false),
                        new Transition("tasty cherry", false));
    }

    @Test
    public void givenTwoInOneOutSingleLetter_whenFrom_thenPlaceWithTwoInOneOut() {
        // given
        String placeDescription = "a, b | c";

        // when
        Place placeActual = Place.from(placeDescription);

        // then
        Assert.assertEquals(2, placeActual.getInputTransitions().size());
        Assert.assertEquals(1, placeActual.getOutputTransitions().size());
        Assertions.assertThat(placeActual.getInputTransitions())
                .containsExactlyInAnyOrder(new Transition("a", false),
                        new Transition("b", false));
        Assertions.assertThat(placeActual.getOutputTransitions())
                .containsExactly(new Transition("c", false));
    }

    @Test
    public void givenNoInNoOut_whenFrom_thenPlaceWithNoInNoOut() {
        // given
        String placeDescriptionEmptySpaces = "   |    ";
        String placeDescriptionNoSpaces = "|";

        // when
        Place placeActualEmptySpaces = Place.from(placeDescriptionEmptySpaces);
        Place placeActualNoSpaces = Place.from(placeDescriptionNoSpaces);

        // then
        Assert.assertEquals(0, placeActualEmptySpaces.getInputTransitions().size());
        Assert.assertEquals(0, placeActualEmptySpaces.getOutputTransitions().size());

        Assert.assertEquals(0, placeActualNoSpaces.getInputTransitions().size());
        Assert.assertEquals(0, placeActualNoSpaces.getOutputTransitions().size());
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
        String placeDescription = "a, b | c | d";

        try { // when
            Place.from(placeDescription);
        }
        catch (IllegalArgumentException e) { // then
            Assert.assertTrue(e.getMessage().contains("The place string should include a single |."));
        }
    }

    @Test
    public void givenNoDelimiters_whenFrom_thenThrowIllegalArgument() {
        // given
        String placeDescription = "a, b";

        try { // when
            Place.from(placeDescription);
        }
        catch (IllegalArgumentException e) { // then
            Assert.assertTrue(e.getMessage().contains("The place string should include a single |."));
        }
    }

    @Test
    public void givenEmptyString_whenFrom_thenThrowIllegalArgument() {
        // given
        String placeDescription = "";

        try { // when
            Place.from(placeDescription);
        }
        catch (IllegalArgumentException e) { // then
            Assert.assertTrue(e.getMessage().contains("The place string should include a single |."));
        }
    }
}
