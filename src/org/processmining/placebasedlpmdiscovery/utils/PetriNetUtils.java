package org.processmining.placebasedlpmdiscovery.utils;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.PetrinetEdge;
import org.processmining.models.graphbased.directed.petrinet.PetrinetNode;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;

import java.util.*;
import java.util.stream.Collectors;

public class PetriNetUtils {

    public static AcceptingPetriNet getWorkflowNetRepresentation(AcceptingPetriNet apn) {
//        Map<PetrinetNode, DependencyExpression> netDependencies = DirectDependencyComputer.computeAll(apn);
//
//        // compute the transitions with no input constraints and those with no output constraints
//        Set<Transition> inTransitions = apn.getNet().getTransitions().stream()
//                .filter(t -> apn.getNet().getInEdges(t).isEmpty()).collect(Collectors.toSet());
//        Set<Transition> outTransitions = apn.getNet().getTransitions().stream()
//                .filter(t -> apn.getNet().getOutEdges(t).isEmpty()).collect(Collectors.toSet());
//
//        // if there are multiple
//        if (inTransitions.size() > 1) {
//
//        }

        throw new NotImplementedException("getWorkflowNetRepresentation is not implemented yet. It should transform " +
                "the given AcceptingPetriNet into a workflow net representation if possible.");
    }

    /**
     * Checks whether the net is a (structural) workflow net: it has exactly one source place
     * with no incoming arcs, exactly one sink place with no outgoing arcs, and every node
     * (place or transition) lies on a path from the source to the sink.
     */
    public static boolean isWorkflowNet(AcceptingPetriNet apn) {
        Petrinet net = apn.getNet();

        List<Place> sources = net.getPlaces().stream()
                .filter(place -> net.getInEdges(place).isEmpty())
                .collect(Collectors.toList());
        List<Place> sinks = net.getPlaces().stream()
                .filter(place -> net.getOutEdges(place).isEmpty())
                .collect(Collectors.toList());

        if (sources.size() != 1 || sinks.size() != 1) {
            return false;
        }

        Set<PetrinetNode> allNodes = net.getNodes();
        Set<PetrinetNode> reachableFromSource = reachableForward(net, sources.get(0));
        Set<PetrinetNode> reachingSink = reachableBackward(net, sinks.get(0));

        return reachableFromSource.containsAll(allNodes) && reachingSink.containsAll(allNodes);
    }

    /**
     * Returns all Petri net nodes (places and transitions) that are reachable from a specific node in the net.
     */
    private static Set<PetrinetNode> reachableForward(Petrinet net, PetrinetNode start) {
        Set<PetrinetNode> visited = new HashSet<>();
        Deque<PetrinetNode> queue = new ArrayDeque<>();
        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            PetrinetNode current = queue.poll();
            for (PetrinetEdge<?, ?> edge : net.getOutEdges(current)) {
                PetrinetNode neighbor = edge.getTarget();
                if (visited.add(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }

        return visited;
    }

    /**
     * Returns all Petri net nodes (places and transitions) that can reach a specific node in the net.
     */
    private static Set<PetrinetNode> reachableBackward(Petrinet net, PetrinetNode start) {
        Set<PetrinetNode> visited = new HashSet<>();
        Deque<PetrinetNode> queue = new ArrayDeque<>();
        visited.add(start);
        queue.add(start);

        while (!queue.isEmpty()) {
            PetrinetNode current = queue.poll();
            for (PetrinetEdge<?, ?> edge : net.getInEdges(current)) {
                PetrinetNode neighbor = edge.getSource();
                if (visited.add(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }

        return visited;
    }

}
