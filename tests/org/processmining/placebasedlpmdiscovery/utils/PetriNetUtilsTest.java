package org.processmining.placebasedlpmdiscovery.utils;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class PetriNetUtilsTest {

    private static AcceptingPetriNet acceptingPetriNetFrom(String lpmDescription) {
        return LocalProcessModelUtils.getAcceptingPetriNetRepresentation(LocalProcessModel.from(lpmDescription));
    }

    @Test
    public void givenSimpleSequenceWithUniqueSourceAndSink_whenIsWorkflowNet_thenTrue() {
        // given
        AcceptingPetriNet apn = acceptingPetriNetFrom("(|a)(a|b)(b|)");

        // when/then
        Assertions.assertThat(PetriNetUtils.isWorkflowNet(apn)).isTrue();
    }

    @Test
    public void givenChoiceSplitWithOrJoin_whenIsWorkflowNet_thenTrue() {
        // given: source place offers a choice between a and b, both feed a shared place that
        // OR-joins into c, which leads to the sink.
        AcceptingPetriNet apn = acceptingPetriNetFrom("(|a,b)(a,b|c)(c|)");

        // when/then
        Assertions.assertThat(PetriNetUtils.isWorkflowNet(apn)).isTrue();
    }

    @Test
    public void givenAndSplitWithAndJoin_whenIsWorkflowNet_thenTrue() {
        // given: a forks into two parallel branches (b and c), which both need to complete
        // before d, which leads to the sink.
        AcceptingPetriNet apn = acceptingPetriNetFrom("(|a)(a|b)(a|c)(b|d)(c|d)(d|)");

        // when/then
        Assertions.assertThat(PetriNetUtils.isWorkflowNet(apn)).isTrue();
    }

    @Test
    public void givenTwoSourcePlaces_whenIsWorkflowNet_thenFalse() {
        // given: two places with no incoming arcs, so there is no unique source place.
        AcceptingPetriNet apn = acceptingPetriNetFrom("(|a)(|b)(a|c)(b|c)(c|)");

        // when/then
        Assertions.assertThat(PetriNetUtils.isWorkflowNet(apn)).isFalse();
    }

    @Test
    public void givenTwoSinkPlaces_whenIsWorkflowNet_thenFalse() {
        // given: a splits into b and c, each ending in its own place with no outgoing arcs,
        // so there is no unique sink place.
        AcceptingPetriNet apn = acceptingPetriNetFrom("(|a)(a|b)(a|c)(b|)(c|)");

        // when/then
        Assertions.assertThat(PetriNetUtils.isWorkflowNet(apn)).isFalse();
    }

    @Test
    public void givenNoSourcePlaceBecauseOfACycle_whenIsWorkflowNet_thenFalse() {
        // given: every place has an incoming arc (a-b-c cycle back into a), so there is no
        // place without incoming arcs to serve as the source.
        AcceptingPetriNet apn = acceptingPetriNetFrom("(a|b)(b|a,c)(c|)");

        // when/then
        Assertions.assertThat(PetriNetUtils.isWorkflowNet(apn)).isFalse();
    }

    @Test
    public void givenNoSinkPlaceBecauseOfACycle_whenIsWorkflowNet_thenFalse() {
        // given: a and b cycle back into each other, so every place has an outgoing arc and
        // there is no sink place.
        AcceptingPetriNet apn = acceptingPetriNetFrom("(|a)(a|b)(b|a)");

        // when/then
        Assertions.assertThat(PetriNetUtils.isWorkflowNet(apn)).isFalse();
    }

    @Test
    public void givenDeadEndBranchThatCannotReachTheSink_whenIsWorkflowNet_thenFalse() {
        // given: besides the main a-b path to the sink, a also feeds a dead-end transition x
        // that has nowhere to go, so x can never reach the sink.
        AcceptingPetriNet apn = acceptingPetriNetFrom("(|a)(a|b)(b|)(a|x)");

        // when/then
        Assertions.assertThat(PetriNetUtils.isWorkflowNet(apn)).isFalse();
    }

    @Test
    public void givenBranchUnreachableFromTheSource_whenIsWorkflowNet_thenFalse() {
        // given: besides the main a-b path from the source, an extra place is fed by y, a
        // transition with no incoming arcs of its own, so y is never reachable from the source.
        AcceptingPetriNet apn = acceptingPetriNetFrom("(|a)(a|b)(b|)(y|b)");

        // when/then
        Assertions.assertThat(PetriNetUtils.isWorkflowNet(apn)).isFalse();
    }
}