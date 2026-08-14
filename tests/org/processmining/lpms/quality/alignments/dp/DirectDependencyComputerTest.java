package org.processmining.lpms.quality.alignments.dp;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.dp.dependency.*;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.util.Arrays;
import java.util.Map;

public class DirectDependencyComputerTest {

    private static AcceptingPetriNet acceptingPetriNetFrom(String lpmDescription) {
        return LocalProcessModelUtils.getAcceptingPetriNetRepresentation(LocalProcessModel.from(lpmDescription));
    }

    private static Transition transitionWithLabel(Map<Transition, DependencyExpression> dependencies, String label) {
        return dependencies.keySet().stream()
                .filter(t -> t.getLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No transition with label " + label));
    }

    @Test
    public void givenSequenceAbc_whenCompute_thenEachDependsOnItsPredecessor() {
        // given
        AcceptingPetriNet apn = acceptingPetriNetFrom("(a | b)(b | c)");

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition a = transitionWithLabel(dependencies, "a");
        Transition b = transitionWithLabel(dependencies, "b");
        Transition c = transitionWithLabel(dependencies, "c");

        Assertions.assertThat(dependencies.get(a)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(b)).isEqualTo(new ActivityDependency(a));
        Assertions.assertThat(dependencies.get(c)).isEqualTo(new ActivityDependency(b));
    }

    @Test
    public void givenChoiceJoinAOrBThenC_whenCompute_thenCDependsOnAOrB() {
        // given
        AcceptingPetriNet apn = acceptingPetriNetFrom("(a, b | c)");

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition a = transitionWithLabel(dependencies, "a");
        Transition b = transitionWithLabel(dependencies, "b");
        Transition c = transitionWithLabel(dependencies, "c");

        Assertions.assertThat(dependencies.get(a)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(b)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(c)).isEqualTo(
                new OrDependency(Arrays.asList(new ActivityDependency(a), new ActivityDependency(b))));
    }

    @Test
    public void givenConcurrencyJoinAAndBThenC_whenCompute_thenCDependsOnAAndB() {
        // given
        AcceptingPetriNet apn = acceptingPetriNetFrom("(a | c)(b | c)");

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition a = transitionWithLabel(dependencies, "a");
        Transition b = transitionWithLabel(dependencies, "b");
        Transition c = transitionWithLabel(dependencies, "c");

        Assertions.assertThat(dependencies.get(a)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(b)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(c)).isEqualTo(
                new AndDependency(Arrays.asList(new ActivityDependency(a), new ActivityDependency(b))));
    }

    @Test
    public void givenSelfLoopPlace_whenCompute_thenTransitionDependsOnItself() {
        // given
        AcceptingPetriNet apn = acceptingPetriNetFrom("(a | a)");

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition a = transitionWithLabel(dependencies, "a");
        Assertions.assertThat(dependencies.get(a)).isEqualTo(new ActivityDependency(a));
    }

    @Test
    public void givenPlaceWithInputAbAndOutputBc_whenCompute_thenBAndCDependOnAOrB() {
        // given
        AcceptingPetriNet apn = acceptingPetriNetFrom("(a, b | b, c)");

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition a = transitionWithLabel(dependencies, "a");
        Transition b = transitionWithLabel(dependencies, "b");
        Transition c = transitionWithLabel(dependencies, "c");

        OrDependency aOrB = new OrDependency(Arrays.asList(new ActivityDependency(a), new ActivityDependency(b)));
        Assertions.assertThat(dependencies.get(a)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(b)).isEqualTo(aOrB);
        Assertions.assertThat(dependencies.get(c)).isEqualTo(aOrB);
    }

    @Test
    public void givenTwoPlacesAToCAndBToBc_whenCompute_thenBDependsOnItselfAndCDependsOnAAndB() {
        // given
        AcceptingPetriNet apn = acceptingPetriNetFrom("(a | c)(b | b, c)");

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition a = transitionWithLabel(dependencies, "a");
        Transition b = transitionWithLabel(dependencies, "b");
        Transition c = transitionWithLabel(dependencies, "c");

        Assertions.assertThat(dependencies.get(a)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(b)).isEqualTo(new ActivityDependency(b));
        Assertions.assertThat(dependencies.get(c)).isEqualTo(
                new AndDependency(Arrays.asList(new ActivityDependency(a), new ActivityDependency(b))));
    }

    @Test
    public void givenMarkedPlaceWithInputAOutputB_whenCompute_thenBDependsOnAOrSingleExecution() {
        // given
        Place place = Place.from("a | b");
        place.setNumTokens(1);
        AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(new LocalProcessModel(place));

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition a = transitionWithLabel(dependencies, "a");
        Transition b = transitionWithLabel(dependencies, "b");

        Assertions.assertThat(dependencies.get(a)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(b)).isEqualTo(
                new OrDependency(Arrays.asList(new ActivityDependency(a), SingleExecutionDependency.INSTANCE)));
    }

    @Test
    public void givenJoinNetworkWithFeedbackThroughT_whenCompute_thenDDependsOnASharedBetweenTwoOrJoins() {
        // given: a marked source place feeds z; z and t jointly feed a place consumed by both x and y;
        // x forks into separate places feeding b and c; y feeds a; a joins with b (one place) and with c
        // (another place) to produce d; d feeds t, closing a cycle back through the "z, t" place.
        Place source = Place.from("| z");
        source.setNumTokens(1);

        LocalProcessModel lpm = LocalProcessModel.from("(z,t|x, y)(x|b)(x|c)(y|a)(a, b|d)(a, c|d)(d|t)");
        lpm.addPlace(source);

        AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition z = transitionWithLabel(dependencies, "z");
        Transition t = transitionWithLabel(dependencies, "t");
        Transition x = transitionWithLabel(dependencies, "x");
        Transition y = transitionWithLabel(dependencies, "y");
        Transition a = transitionWithLabel(dependencies, "a");
        Transition b = transitionWithLabel(dependencies, "b");
        Transition c = transitionWithLabel(dependencies, "c");
        Transition d = transitionWithLabel(dependencies, "d");

        OrDependency zOrT = new OrDependency(Arrays.asList(new ActivityDependency(z), new ActivityDependency(t)));

        Assertions.assertThat(dependencies.get(z)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(t)).isEqualTo(new ActivityDependency(d));
        Assertions.assertThat(dependencies.get(x)).isEqualTo(zOrT);
        Assertions.assertThat(dependencies.get(y)).isEqualTo(zOrT);
        Assertions.assertThat(dependencies.get(b)).isEqualTo(new ActivityDependency(x));
        Assertions.assertThat(dependencies.get(c)).isEqualTo(new ActivityDependency(x));
        Assertions.assertThat(dependencies.get(a)).isEqualTo(new ActivityDependency(y));
        Assertions.assertThat(dependencies.get(d)).isEqualTo(new AndDependency(Arrays.asList(
                new OrDependency(Arrays.asList(new ActivityDependency(a), new ActivityDependency(b))),
                new OrDependency(Arrays.asList(new ActivityDependency(a), new ActivityDependency(c))))));
    }

    @Test
    public void givenAndJoinWithOneOrBranchAndOneSingleBranch_whenCompute_thenEDependsOnBOrCAndD() {
        // given: a marked source place feeds a; a forks into a place joining b and c, and
        // separately into a place feeding only d; e is fed by a place joining b/c and a
        // separate place fed only by d.
        Place source = Place.from("| a");
        source.setNumTokens(1);

        LocalProcessModel lpm = LocalProcessModel.from("(a |b, c)(a|d)(b, c|e)(d|e)(e|)");
        lpm.addPlace(source);

        AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition a = transitionWithLabel(dependencies, "a");
        Transition b = transitionWithLabel(dependencies, "b");
        Transition c = transitionWithLabel(dependencies, "c");
        Transition d = transitionWithLabel(dependencies, "d");
        Transition e = transitionWithLabel(dependencies, "e");

        Assertions.assertThat(dependencies.get(a)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(b)).isEqualTo(new ActivityDependency(a));
        Assertions.assertThat(dependencies.get(c)).isEqualTo(new ActivityDependency(a));
        Assertions.assertThat(dependencies.get(d)).isEqualTo(new ActivityDependency(a));
        Assertions.assertThat(dependencies.get(e)).isEqualTo(new AndDependency(Arrays.asList(
                new OrDependency(Arrays.asList(new ActivityDependency(b), new ActivityDependency(c))),
                new ActivityDependency(d))));
    }

    @Test
    public void givenTwoCycleBetweenBAndC_whenCompute_thenBAndCAreMutuallyDependent() {
        // given: a marked source place feeds a; b is fed by a place joining a and c, and c is fed
        // only by b - so b and c are mutually dependent, a 2-cycle extending the self-loop case.
        Place source = Place.from("| a");
        source.setNumTokens(1);

        LocalProcessModel lpm = LocalProcessModel.from("(a, c|b)(b |c)");
        lpm.addPlace(source);

        AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition a = transitionWithLabel(dependencies, "a");
        Transition b = transitionWithLabel(dependencies, "b");
        Transition c = transitionWithLabel(dependencies, "c");

        Assertions.assertThat(dependencies.get(a)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(b)).isEqualTo(
                new OrDependency(Arrays.asList(new ActivityDependency(a), new ActivityDependency(c))));
        Assertions.assertThat(dependencies.get(c)).isEqualTo(new ActivityDependency(b));
    }

    @Test
    public void givenSharedStartChoiceWithAndJoins_whenCompute_thenDAndEDependOnRedundantAndOfOrAndSingleBranch() {
        // given: a single marked place is shared by both a and b (a choice of which fires); d is fed
        // by a place joining a/b and a separate place fed only by a, so d's dependency structurally
        // repeats a (AND(OR(a, b), a) - not simplified by absorption); e mirrors this shape with b.
        Place source = Place.from("| a, b");
        source.setNumTokens(1);

        LocalProcessModel lpm = LocalProcessModel.from("(a,b |d, e)(a|d)(b|e)(d, e|)");
        lpm.addPlace(source);

        AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);

        // when
        Map<Transition, DependencyExpression> dependencies = DirectDependencyComputer.compute(apn);

        // then
        Transition a = transitionWithLabel(dependencies, "a");
        Transition b = transitionWithLabel(dependencies, "b");
        Transition d = transitionWithLabel(dependencies, "d");
        Transition e = transitionWithLabel(dependencies, "e");

        OrDependency aOrB = new OrDependency(Arrays.asList(new ActivityDependency(a), new ActivityDependency(b)));

        Assertions.assertThat(dependencies.get(a)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(b)).isEqualTo(SingleExecutionDependency.INSTANCE);
        Assertions.assertThat(dependencies.get(d)).isEqualTo(
                new AndDependency(Arrays.asList(aOrB, new ActivityDependency(a))));
        Assertions.assertThat(dependencies.get(e)).isEqualTo(
                new AndDependency(Arrays.asList(aOrB, new ActivityDependency(b))));
    }
}