package org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowLog;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.FPGrowthPlaceFollowGraph;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.*;
import java.util.stream.Collectors;

public class FPGrowthPlaceFollowGraphBuilder {

    private final XLog log;
    private final Set<Place> places;
    private final int maxDependencyLength;

    public FPGrowthPlaceFollowGraphBuilder(XLog log, Set<Place> places, int maxDependencyLength) {
        this.log = log;
        this.places = places;
        this.maxDependencyLength = maxDependencyLength;
    }

    public FPGrowthPlaceFollowGraph buildGraph() {
        FPGrowthPlaceFollowGraph graph = new FPGrowthPlaceFollowGraph();

        Set<String> transitions = PlaceUtils.getAllTransitions(this.places)
                .stream()
                .map(Transition::getLabel)
                .collect(Collectors.toSet());
        Map<String, Set<Place>> inTransitionPlacesMap = PlaceUtils.getTransitionPlaceSetMapping(transitions,
                this.places, true, transitions.stream().collect(Collectors.toMap(t -> t, t -> t)), true);
        Map<String, Set<Place>> outTransitionPlacesMap = PlaceUtils.getTransitionPlaceSetMapping(transitions,
                this.places, false, transitions.stream().collect(Collectors.toMap(t -> t, t -> t)), true);

        WindowLog windowLog = new WindowLog(this.log);

        // iterate through all variants
        for (Integer traceVariantId : windowLog.getTraceVariantIds()) {
            List<Integer> traceVariant = windowLog.getTraceVariant(traceVariantId);
            Integer count = windowLog.getTraceVariantCount(traceVariant);

            Queue<Map.Entry<Place, Integer>> queue = new LinkedList<>(); // create place queue
            for (Integer event : traceVariant) { // iterate through the events in the trace variant
                // iterate through all places that have an input transition with a label as the event activity
                Set<Place> candidatePlaces = inTransitionPlacesMap.getOrDefault(
                        windowLog.getReverseLabelMap().get(event), new HashSet<>());
                for (Place p : candidatePlaces) {
                    // iterate through all places in the queue
                    for (Map.Entry<Place, Integer> queueEntry : queue) {
                        Place queuePlace = queueEntry.getKey();
                        Integer weight = queueEntry.getValue();

                        // if the event is output transition in the queue place add the edge
                        if (outTransitionPlacesMap.get(windowLog.getReverseLabelMap().get(event)).contains(queuePlace))
                            graph.addEdge(queuePlace, p, weight * count);
                    }
                }

                // update queue entries
                queue = queue
                        .stream()
                        .filter(e -> e.getValue() > 1)
                        .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue() - 1))
                        .collect(Collectors.toCollection(LinkedList::new));

                // add new places together with places that these places are connected via silent transition
                for (Place p : candidatePlaces) {
                    queue.add(new AbstractMap.SimpleEntry<>(p, this.maxDependencyLength)); // add place

                    // find all output transitions of the place that are silent
                    Set<Transition> silentOutputTransitions = p.getSilentTransitions(false);

                    // iterate the silent transitions
                    for (Transition silent : silentOutputTransitions) {
                        // find candidate places that can be added for the silent transition
                        Set<Place> candidatePlacesViaSilent = inTransitionPlacesMap.getOrDefault(
                                silent.getLabel(), new HashSet<>());
                        // iterate the candidate places via silent transitions
                        for (Place pViaSilent : candidatePlacesViaSilent) {
                            graph.addEdge(p, pViaSilent, this.maxDependencyLength * count); // add edge

                            queue.add(new AbstractMap.SimpleEntry<>(pViaSilent, this.maxDependencyLength)); // add place
                        }
                    }
                }

            }
        }
        return graph;
    }
}
