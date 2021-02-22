package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.fpgrowth;

import org.deckfour.xes.model.XLog;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.logs.WindowLog;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.Place;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.Transition;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.fpgrowth.FPGrowthPlaceFollowGraph;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.utils.PlaceUtils;

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

                // add new places
                for (Place p : candidatePlaces)
                    queue.add(new AbstractMap.SimpleEntry<>(p, this.maxDependencyLength));

            }
        }
        return graph;
    }
}
