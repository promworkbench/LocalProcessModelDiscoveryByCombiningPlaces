package org.processmining.placebasedlpmdiscovery.model.logs.traces;

import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;

import java.util.ArrayList;
import java.util.Collection;

public class ConvenienceEventLogTraceExtractor implements EventLogTraceExtractor {


    @Override
    public Collection<EventLogTrace<?>> getTraces(EventLog eventLog, String attributeKey) {
        Collection<EventLogTrace<?>> traces = new ArrayList<>();
        if (eventLog instanceof XLogWrapper) {
            XLogWrapper cEventLog = (XLogWrapper) eventLog;
            cEventLog.getOriginalLog().forEach(xTrace -> traces.add(new XTraceWrapper(xTrace)));
        }
        return traces;
    }
}
