package org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.extractors;

public interface EventLogTraceVariantExtractor {

    static ActivityBasedTotallyOrderedEventLogTraceVariantExtractor getActivityBasedTotallyOrdered(String attributeKey) {
        return new ActivityBasedTotallyOrderedEventLogTraceVariantExtractor(attributeKey);
    }
}
