package org.processmining.placebasedlpmdiscovery.model.logs.activities;

import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.EventLogTraceVariantElement;

public interface Activity extends EventLogTraceVariantElement {

    String getName();

    ActivityId getId();
}
