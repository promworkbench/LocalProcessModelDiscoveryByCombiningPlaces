package org.processmining.placebasedlpmdiscovery.model.logs.activities;

import org.processmining.placebasedlpmdiscovery.model.exporting.gson.GsonSerializable;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.EventLogTraceVariantElement;

public interface Activity extends EventLogTraceVariantElement, GsonSerializable {

    String getName();

    ActivityId getId();
}
