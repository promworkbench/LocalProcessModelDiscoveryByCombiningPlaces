package org.processmining.lpms.quality.alignments.dp;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.dp.dependency.*;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;

import java.util.*;
import java.util.stream.Collectors;

public final class DirectDependencyComputer {

    private DirectDependencyComputer() {
    }

    /**
     * Computes, for every transition in the net, a boolean expression over the other
     * transitions it directly depends on structurally: a transition needs a token in
     * every one of its input places (AND across places), and each place's token can come
     * from any of its producing transitions, or from the initial marking if the place
     * already holds a token there (OR across producers and the initial marking).
     */
    public static Map<Transition, DependencyExpression> computeTransitionsOnly(AcceptingPetriNet apn) {
        Petrinet net = apn.getNet();
        Marking initialMarking = apn.getInitialMarking();
        Map<Transition, DependencyExpression> result = new HashMap<>();

        for (Transition transition : net.getTransitions()) {
            result.put(transition, dependencyForTransition(net, initialMarking, transition));
        }

        return result;
    }

    /**
     * Computes, for every node in the net (transitions and places), a direct boolean
     * dependency expression over the other kind of node: a transition depends on an AND of
     * its input places, and a place depends on an OR of its producing transitions, plus the
     * trivially-enabled option if the place already holds a token in the initial marking.
     * Unlike {@link #computeTransitionsOnly}, dependencies are not flattened past the
     * immediate predecessor.
     */
    public static Map<PetrinetNode, DependencyExpression> computeAll(AcceptingPetriNet apn) {
        Petrinet net = apn.getNet();
        Marking initialMarking = apn.getInitialMarking();
        Map<PetrinetNode, DependencyExpression> result = new HashMap<>();

        for (Transition transition : net.getTransitions()) {
            result.put(transition, directDependencyForTransition(net, transition));
        }

        for (Place place : net.getPlaces()) {
            DependencyExpression placeDependency = dependencyForPlace(net, initialMarking, place);
            result.put(place, placeDependency != null ? placeDependency : NoDependency.INSTANCE);
        }

        return result;
    }

    /**
     * The dependency expression describing what is needed to have a token in every place of
     * {@code marking} simultaneously: the AND, across the marking's distinct places, of each
     * place's own dependency (see {@link #dependencyForPlace}). Places are considered without
     * multiplicity, consistent with {@link #computeTransitionsOnly}'s treatment of arcs as weight-1.
     */
    public static DependencyExpression computeForMarking(AcceptingPetriNet apn, Marking marking) {
        Petrinet net = apn.getNet();
        Marking initialMarking = apn.getInitialMarking();

        List<DependencyExpression> andChildren = new ArrayList<>();
        for (Place place : marking.baseSet()) {
            DependencyExpression placeDependency = dependencyForPlace(net, initialMarking, place);
            if (placeDependency != null) {
                andChildren.add(placeDependency);
            }
        }

        // TODO: in general, an empty marking should be NoDependency, but for now we assume it is
        //  enabled once, consistent with compute()'s treatment of transitions with no input places
        if (andChildren.isEmpty()) {
            return null;
        }
        return andChildren.size() == 1 ? andChildren.get(0) : new AndDependency(andChildren);
    }

    /**
     * The dependency expression describing what {@code transition} directly depends on
     * without flattening past its immediate predecessors: the AND, across its input places,
     * of a {@link PlaceDependency} leaf for each one. Arcs are treated as weight-1, without
     * multiplicity.
     */
    private static DependencyExpression directDependencyForTransition(Petrinet net, Transition transition) {
        List<PetrinetEdge<?, ?>> inEdges = new ArrayList<>(net.getInEdges(transition));

        // TODO: in general, this should be NoDependency, but for now we assume that transitions with no
        //  input places are enabled once
        if (inEdges.isEmpty()) {
            return SingleExecutionDependency.INSTANCE;
        }

        List<DependencyExpression> andChildren = inEdges.stream()
                .map(inEdge -> (Place) inEdge.getSource())
                .map(PlaceDependency::new)
                .collect(Collectors.toList());
        return andChildren.size() == 1 ? andChildren.get(0) : new AndDependency(andChildren);
    }

    /**
     * The dependency expression describing what is needed for {@code transition} to be
     * enabled: the AND, across its input places, of each place's own dependency (see
     * {@link #dependencyForPlace}). Arcs are treated as weight-1, without multiplicity.
     */
    private static DependencyExpression dependencyForTransition(Petrinet net, Marking initialMarking,
                                                                Transition transition) {
        DependencyExpression direct = directDependencyForTransition(net, transition);
        if (direct instanceof SingleExecutionDependency) {
            return direct;
        }

        List<PlaceDependency> placeLeaves = direct instanceof AndDependency
                ? ((AndDependency) direct).getChildren().stream()
                .map(child -> (PlaceDependency) child)
                .collect(Collectors.toList())
                : Collections.singletonList((PlaceDependency) direct);

        List<DependencyExpression> andChildren = new ArrayList<>();
        for (PlaceDependency placeLeaf : placeLeaves) {
            DependencyExpression placeDependency = dependencyForPlace(net, initialMarking, placeLeaf.getPlace());
            if (placeDependency != null) {
                andChildren.add(placeDependency);
            }
        }
        return andChildren.size() == 1 ? andChildren.get(0) : new AndDependency(andChildren);
    }

    /**
     * The dependency expression for a single place holding a token: an OR across the
     * transitions that can produce a token there, plus the trivially-enabled option if the
     * place already holds a token in the initial marking. {@code null} if neither applies.
     */
    private static DependencyExpression dependencyForPlace(Petrinet net, Marking initialMarking, Place place) {
        List<DependencyExpression> orChildren = net.getInEdges(place).stream()
                .map(edge -> (Transition) edge.getSource())
                .map(ActivityDependency::new)
                .collect(Collectors.toList());

        if (initialMarking.contains(place)) {
            orChildren.add(SingleExecutionDependency.INSTANCE);
        }

        if (orChildren.isEmpty()) {
            return null;
        }
        return orChildren.size() == 1 ? orChildren.get(0) : new OrDependency(orChildren);
    }
}