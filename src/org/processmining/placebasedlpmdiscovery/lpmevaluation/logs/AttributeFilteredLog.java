package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.EventAttributeSummary;

import java.util.Map;

public class AttributeFilteredLog extends AbstractOptionalEventInclusionLog {

    private Map<String, EventAttributeSummary<?,?>> attributeSummary;

    public AttributeFilteredLog(XLog log) {
        super(log);
    }

    @Override
    public boolean isEventIncluded(XEvent event) {
        for (XAttribute attribute : event.getAttributes().values()) {
            if (!attributeSummary.get(attribute.getKey()).acceptValue(attribute))
                return false;
        }
        return true;
    }
}
