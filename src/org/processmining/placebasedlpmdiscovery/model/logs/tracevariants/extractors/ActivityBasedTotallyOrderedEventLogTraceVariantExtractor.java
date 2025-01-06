package org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.extractors;

import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.traces.EventLogTrace;
import org.processmining.placebasedlpmdiscovery.model.logs.traces.EventLogTraceExtractor;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.ActivityBasedTotallyOrderedEventLogTraceVariant;

import java.util.*;

public class ActivityBasedTotallyOrderedEventLogTraceVariantExtractor implements TotallyOrderedEventLogTraceVariantExtractor {

    private final String attributeKey;

    public ActivityBasedTotallyOrderedEventLogTraceVariantExtractor(String attributeKey) {
        this.attributeKey = attributeKey;
    }

    public Collection<ActivityBasedTotallyOrderedEventLogTraceVariant> extract(EventLog eventLog) {
        Set<ActivityBasedTotallyOrderedEventLogTraceVariant> variants = new HashSet<>();

        Collection<EventLogTrace<?>> traces = EventLogTraceExtractor.getInstance().getTraces(eventLog, attributeKey);
        for (EventLogTrace<?> trace : traces) {
            List<Activity> variantKey = new ArrayList<>();
            trace.forEach(event -> variantKey.add(event.getActivity()));
            ActivityBasedTotallyOrderedEventLogTraceVariant variant =
                    new ActivityBasedTotallyOrderedEventLogTraceVariant(variantKey);

            Optional<ActivityBasedTotallyOrderedEventLogTraceVariant> existingVariant =
                    variants.stream().filter(v -> v.equals(variant)).findFirst();
            if (existingVariant.isPresent()) {
                existingVariant.get().addTrace(trace);
            } else {
                variant.addTrace(trace);
                variants.add(variant);
            }
        }

        return variants;
    }
}
