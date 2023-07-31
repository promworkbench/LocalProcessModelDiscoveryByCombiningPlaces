package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced;

import org.deckfour.xes.extension.std.XConceptExtension;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.extra.AbstractActivityMapping;

import java.util.*;

public abstract class AbstractRemappedActivitiesLog<T> extends AbstractEnhancedLog implements RemappedActivitiesLog<T>{

    // The mapping from the original activities in the event log to the integer labels
    protected AbstractActivityMapping<T> mapping;

    // To every trace variant we have given an id
    private Map<Integer, List<T>> traceVariantIdsMap;

    // Map of trace variant and the number of times it appears in the log.
    // The trace variant is represented as list of integers, where the integers are taken from the label map.
    private Map<List<T>, Integer> traceVariants;

    private Map<List<T>, Set<XTrace>> backToOriginalMapping;

    public AbstractRemappedActivitiesLog(XLog log) {
        super(log);
    }

    protected abstract AbstractActivityMapping<T> createMapping();

    @Override
    protected void makeLog() {
        this.mapping = this.createMapping();
        this.traceVariants = new HashMap<>();

        for (XTrace trace : this.originalLog) {
            List<T> eventList = new ArrayList<>();
            for (XEvent event : trace) {
                // check if the event has the concept:name attribute which is the label of the event
                if (!event.getAttributes().containsKey(XConceptExtension.KEY_NAME))
                    continue;

                // get mapping for label
                XAttributeLiteral eventLabel = (XAttributeLiteral) event.getAttributes().get(XConceptExtension.KEY_NAME);
                eventList.add(this.mapping.getMapping(eventLabel.getValue()));
            }
            int count = this.traceVariants.getOrDefault(eventList, 0);
            this.traceVariants.put(eventList, count + 1);
            this.backToOriginalMapping.getOrDefault(eventList, new HashSet<>()).add(trace);
        }

        this.traceVariantIdsMap = new HashMap<>();
        int traceVariantId = 0;
        for (List<T> traceVariant : this.traceVariants.keySet())
            this.traceVariantIdsMap.put(++traceVariantId, traceVariant);
    }

    @Override
    public AbstractActivityMapping<T> getMapping() {
        return mapping;
    }

    public Set<Integer> getTraceVariantIds() {
        return traceVariantIdsMap.keySet();
    }

    public List<T> getTraceVariant(Integer traceVariantId) {
        return traceVariantIdsMap.get(traceVariantId);
    }

    public Integer getTraceVariantCount(List<T> traceVariant) {
        return traceVariants.get(traceVariant);
    }

    public Integer getTraceCount() {
        return this.traceVariants.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Set<XTrace> getOriginalTraces(Integer traceVariantId) {
        return this.backToOriginalMapping.get(this.traceVariantIdsMap.get(traceVariantId));
    }
}
