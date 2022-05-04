package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.model.XEvent;

public interface OptionalEventInclusionLog {

    boolean isEventIncluded(XEvent event);
}
