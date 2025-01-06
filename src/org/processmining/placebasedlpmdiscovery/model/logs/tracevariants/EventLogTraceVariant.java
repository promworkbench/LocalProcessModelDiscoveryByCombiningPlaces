package org.processmining.placebasedlpmdiscovery.model.logs.tracevariants;

import org.processmining.placebasedlpmdiscovery.model.logs.traces.EventLogTrace;

import java.util.Collection;

public interface EventLogTraceVariant<ELEMENT extends EventLogTraceVariantElement> {

    int getCardinality();

    Collection<EventLogTrace<?>> getTraces();

    int size();

    ELEMENT get(int position);
}
