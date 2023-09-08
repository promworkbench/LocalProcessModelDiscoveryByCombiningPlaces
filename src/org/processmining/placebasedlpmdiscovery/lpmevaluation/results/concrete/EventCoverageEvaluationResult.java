package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.HashMap;
import java.util.Map;

public class EventCoverageEvaluationResult implements LPMEvaluationResult {
    // last event covered map for each transition (doesn't support duplicates at this point) to avoid repetition
    private final Map<String, Pair<Integer, Integer>> lastCoveredEvents;
    // count of covered events for each transition (doesn't support duplicates at this point)
    private final Map<String, Integer> coveredEventsCount;
    private Map<String, Integer> eventCountPerActivity;

    public EventCoverageEvaluationResult(LocalProcessModel lpm) {
        this.lastCoveredEvents = new HashMap<>();
        this.coveredEventsCount = new HashMap<>();
        lpm.getTransitions().forEach(t -> {
            this.lastCoveredEvents.put(t.getLabel(), new Pair<>(-1, -1));
            this.coveredEventsCount.put(t.getLabel(), 0);
        });
    }

    @Override
    public double getResult() {
        return 0;
    }

    @Override
    public double getNormalizedResult() {
        return 0;
    }

    @Override
    public LPMEvaluationResultId getId() {
        return StandardLPMEvaluationResultId.EventCoverageEvaluationResult;
    }

    public boolean isLastCoveredEvent(String activity, Integer traceVariantId, Integer eventPos) {
        Pair<Integer, Integer> eventId = this.lastCoveredEvents.get(activity);
        return eventId.getFirst().equals(traceVariantId) && eventId.getSecond().equals(eventPos);
    }

    public void updateCoveredEventsCount(String activity, int count) {
        this.coveredEventsCount.put(activity, this.coveredEventsCount.get(activity) + count);
    }

    public void updateLastCoveredEvent(String activity, Integer traceVariantId, Integer eventPos) {
        this.lastCoveredEvents.put(activity, new Pair<>(traceVariantId, eventPos));
    }

    public int getCoveredEventsCount(String activity) {
        return this.coveredEventsCount.get(activity);
    }

    public void setEventCountPerActivity(Map<String, Integer> eventCountPerActivity) {
        this.eventCountPerActivity = eventCountPerActivity;
    }

    public Map<String, Integer> getEventCountPerActivity() {
        return eventCountPerActivity;
    }
}
