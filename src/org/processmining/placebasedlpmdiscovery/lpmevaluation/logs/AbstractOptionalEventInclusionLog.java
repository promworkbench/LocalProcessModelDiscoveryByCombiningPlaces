package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractOptionalEventInclusionLog extends AbstractEnhancedLog implements OptionalEventInclusionLog {

    protected List<List<Boolean>> eventInclusionLog;

    public AbstractOptionalEventInclusionLog(XLog log) {
        super(log);
    }

    @Override
    protected void makeLog() {
        eventInclusionLog = new ArrayList<>();
        for (XTrace trace : this.originalLog) {
            List<Boolean> eventInclusionTrace = new ArrayList<>();
            for (XEvent event : trace) {
                eventInclusionTrace.add(this.isEventIncluded(event) ? Boolean.TRUE : Boolean.FALSE);
            }
            eventInclusionLog.add(eventInclusionTrace);
        }
    }

    public abstract boolean isEventIncluded(XEvent event);
}
