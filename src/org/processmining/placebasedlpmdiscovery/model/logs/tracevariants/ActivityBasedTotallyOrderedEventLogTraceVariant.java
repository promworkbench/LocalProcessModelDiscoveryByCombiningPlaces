package org.processmining.placebasedlpmdiscovery.model.logs.tracevariants;


import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.traces.EventLogTrace;

import java.util.*;

public class ActivityBasedTotallyOrderedEventLogTraceVariant implements TotallyOrderedEventLogTraceVariant<Activity> {

    private final List<Activity> key;
    private final transient Collection<EventLogTrace<?>> traces;

    public ActivityBasedTotallyOrderedEventLogTraceVariant(List<Activity> activities) {
        this.key = Collections.unmodifiableList(activities);
        this.traces = new HashSet<>();
    }

    @Override
    public int getCardinality() {
        return this.traces.size();
    }

    @Override
    public Collection<EventLogTrace<?>> getTraces() {
        return this.traces;
    }

    @Override
    public int size() {
        return this.key.size();
    }

    @Override
    public Activity get(int position) {
        return key.get(position);
    }

    @Override
    public Integer getId() {
        return Objects.hash(key.toArray());
    }

    public boolean addTrace(EventLogTrace<?> trace) {
        return this.traces.add(trace);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ActivityBasedTotallyOrderedEventLogTraceVariant variant = (ActivityBasedTotallyOrderedEventLogTraceVariant) o;
        return Objects.equals(key, variant.key);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(key);
    }

}
