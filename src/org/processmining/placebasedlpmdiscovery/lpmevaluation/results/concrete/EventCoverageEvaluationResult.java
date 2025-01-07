package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.EventLogTraceVariant;

import java.util.HashMap;
import java.util.Map;

public class EventCoverageEvaluationResult implements LPMEvaluationResult {
    // last event covered map for each transition (doesn't support duplicates at this point) to avoid repetition
    private final Map<String, Pair<EventLogTraceVariant, Integer>> lastCoveredEvents;
    // count of covered events for each transition (doesn't support duplicates at this point)
    private final Map<String, Integer> coveredEventsCount;
    private Map<String, Integer> eventCountPerActivity;

    public EventCoverageEvaluationResult(LocalProcessModel lpm) {
        this.lastCoveredEvents = new HashMap<>();
        this.coveredEventsCount = new HashMap<>();
        lpm.getTransitions().forEach(t -> {
            this.lastCoveredEvents.put(t.getLabel(), null);
            this.coveredEventsCount.put(t.getLabel(), 0);
        });
    }

    @Override
    public double getResult() {
        double sum = 0;
        for (String act : this.coveredEventsCount.keySet()) {
            sum += this.coveredEventsCount.get(act) * 1.0 / this.eventCountPerActivity.get(act);
        }

        return sum / this.coveredEventsCount.size();
    }

    @Override
    public double getNormalizedResult() {
        return getResult();
    }

    @Override
    public LPMEvaluationResultId getId() {
        return StandardLPMEvaluationResultId.EventCoverageEvaluationResult;
    }

    public boolean isLastCoveredEvent(String activity, EventLogTraceVariant traceVariant, Integer eventPos) {
        Pair<EventLogTraceVariant, Integer> eventId = this.lastCoveredEvents.get(activity);
        return eventId != null && eventId.getLeft().equals(traceVariant) && eventId.getRight().equals(eventPos);
    }

    public void updateCoveredEventsCount(String activity, int count) {
        this.coveredEventsCount.put(activity, this.coveredEventsCount.get(activity) + count);
    }

    public void updateLastCoveredEvent(String activity, EventLogTraceVariant traceVariant, Integer eventPos) {
        this.lastCoveredEvents.put(activity, new ImmutablePair<>(traceVariant, eventPos));
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
