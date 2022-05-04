package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.model.XEvent;

public class LPMSuitableLog implements OptionalEventInclusionLog {

    private final OptionalEventInclusionLog optionalEventInclusionLog;
    private final WindowLog windowLog;

    public LPMSuitableLog(OptionalEventInclusionLog optionalEventInclusionLog, WindowLog windowLog) {
        this.optionalEventInclusionLog = optionalEventInclusionLog;
        this.windowLog = windowLog;
    }

    @Override
    public boolean isEventIncluded(XEvent event) {
        return this.optionalEventInclusionLog.isEventIncluded(event);
    }
}
