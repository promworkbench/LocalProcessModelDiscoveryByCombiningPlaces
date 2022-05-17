package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced;

import org.deckfour.xes.model.XEvent;

public interface OptionalEventInclusionLog {

    boolean isEventIncluded(XEvent event);
}
