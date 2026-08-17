package org.processmining.lpms.quality.alignments.dp;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.dp.dependency.*;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public static Map<Transition, DependencyExpression> compute(AcceptingPetriNet apn) {
        Petrinet net = apn.getNet();
        Marking initialMarking = apn.getInitialMarking();
        Map<Transition, DependencyExpression> result = new HashMap<>();

        for (Transition transition : net.getTransitions()) {
            List<PetrinetEdge<?, ?>> inEdges = new ArrayList<>(net.getInEdges(transition));

            // TODO: in general, this should be NoDependency, but for now we assume that transitions with no
            //  input  places are enabled once
            if (inEdges.isEmpty()) {
                result.put(transition, SingleExecutionDependency.INSTANCE);
                continue;
            }

            List<DependencyExpression> andChildren = new ArrayList<>();
            for (PetrinetEdge<?, ?> inEdge : inEdges) {
                Place inputPlace = (Place) inEdge.getSource();
                DependencyExpression placeDependency = dependencyForPlace(net, initialMarking, inputPlace);
                if (placeDependency != null) {
                    andChildren.add(placeDependency);
                }
            }
            result.put(transition, andChildren.size() == 1 ? andChildren.get(0) : new AndDependency(andChildren));
        }

        return result;
    }

    /**
     * The dependency expression describing what is needed to have a token in every place of
     * {@code marking} simultaneously: the AND, across the marking's distinct places, of each
     * place's own dependency (see {@link #dependencyForPlace}). Places are considered without
     * multiplicity, consistent with {@link #compute}'s treatment of arcs as weight-1.
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