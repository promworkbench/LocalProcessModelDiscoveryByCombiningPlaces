package org.processmining.placebasedlpmdiscovery.model.logs.traces;

import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

import java.util.Collection;

public interface EventLogTraceExtractor {

    static EventLogTraceExtractor getInstance() {
        return new ConvenienceEventLogTraceExtractor();
    }

    Collection<EventLogTrace<?>> getTraces(EventLog eventLog, String attributeKey);
}
