package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.functional;

import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.*;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.enhanced.extra.AbstractActivityMapping;

public class IntegerMappedOptionalEventInclusionLog extends CompositeLog implements OptionalEventInclusionLog, RemappedActivitiesLog<Integer> {

    public IntegerMappedOptionalEventInclusionLog(XLog log) {
        super(new IntegerMappedLog(log), new AttributeFilteredLog(log));
    }

    @Override
    public boolean isEventIncluded(XEvent event) {
        for (AbstractEnhancedLog log : this.logs) {
            if (log instanceof AttributeFilteredLog) {
                return ((AttributeFilteredLog) log).isEventIncluded(event);
            }
        }
        throw new IllegalStateException("There is no enhanced log for optional event inclusion");
    }

    @Override
    public AbstractActivityMapping<Integer> getMapping() {
        for (AbstractEnhancedLog log : this.logs) {
            if (log instanceof IntegerMappedLog) {
                return ((IntegerMappedLog) log).getMapping();
            }
        }
        throw new IllegalStateException("There is no enhanced log for integer mapping");
    }
}
