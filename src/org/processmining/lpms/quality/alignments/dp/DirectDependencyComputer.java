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

                List<DependencyExpression> orChildren = net.getInEdges(inputPlace).stream()
                        .map(edge -> (Transition) edge.getSource())
                        .map(ActivityDependency::new)
                        .collect(Collectors.toList());

                if (initialMarking.contains(inputPlace)) {
                    orChildren.add(SingleExecutionDependency.INSTANCE);
                }

                if (orChildren.isEmpty()) {
                    continue;
                }
                andChildren.add(orChildren.size() == 1 ? orChildren.get(0) : new OrDependency(orChildren));
            }
            result.put(transition, andChildren.size() == 1 ? andChildren.get(0) : new AndDependency(andChildren));
        }

        return result;
    }
}