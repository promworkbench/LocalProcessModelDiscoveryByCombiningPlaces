package org.processmining.lpms.quality.alignments.dp;

import nl.tue.astar.AStarException;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.PNAlignments;
import org.processmining.lpms.quality.alignments.dp.dependency.*;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DPPNAlignments implements PNAlignments {
    /**
     * Computes, for every transition in the net, a boolean expression over the other
     * transitions it directly depends on structurally: a transition needs a token in
     * every one of its input places (AND across places), and each place's token can come
     * from any of its producing transitions, or from the initial marking if the place
     * already holds a token there (OR across producers and the initial marking - though the
     * latter, unlike a producer, only supplies a single free token). A transition with no
     * input places at all is itself only fireable once for free, so it gets a
     * {@link SingleExecutionDependency} rather than depending on nothing. Invisible/tau
     * transitions are not resolved transparently - they appear as literal dependencies.
     */
    static Map<Transition, DependencyExpression> computeDirectDependencies(AcceptingPetriNet apn) {
        Petrinet net = apn.getNet();
        Marking initialMarking = apn.getInitialMarking();
        Map<Transition, DependencyExpression> result = new HashMap<>();

        for (Transition transition : net.getTransitions()) {
            List<PetrinetEdge<?, ?>> inEdges = new ArrayList<>(net.getInEdges(transition));
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

            DependencyExpression dependency;
            if (inEdges.isEmpty()) {
                dependency = SingleExecutionDependency.INSTANCE;
            } else if (andChildren.isEmpty()) {
                dependency = NoDependency.INSTANCE;
            } else if (andChildren.size() == 1) {
                dependency = andChildren.get(0);
            } else {
                dependency = new AndDependency(andChildren);
            }
            result.put(transition, dependency);
        }

        return result;
    }

    @Override
    public PNMatchInstancesRepResult compute(AcceptingPetriNet apn, XLog log) throws AStarException {
        return null;
    }

    @Override
    public PNMatchInstancesRepResult compute(AcceptingPetriNet apn, XTrace trace) throws AStarException {
        return null;
    }
}
