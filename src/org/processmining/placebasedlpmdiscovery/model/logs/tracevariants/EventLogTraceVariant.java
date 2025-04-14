package org.processmining.placebasedlpmdiscovery.model.logs.tracevariants;

import org.processmining.placebasedlpmdiscovery.model.exporting.gson.GsonSerializable;
import org.processmining.placebasedlpmdiscovery.model.logs.traces.EventLogTrace;

import java.util.Collection;

public interface EventLogTraceVariant<ELEMENT extends EventLogTraceVariantElement> extends GsonSerializable {

    int getCardinality();

    Collection<EventLogTrace<?>> getTraces();

    int size();

    ELEMENT get(int position);

    Integer getId();
}
