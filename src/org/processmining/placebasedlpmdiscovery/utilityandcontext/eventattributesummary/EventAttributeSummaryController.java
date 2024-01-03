package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.info.XAttributeInfo;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.*;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventAttributeCollectorResult;

import java.util.*;

/**
 * Class that summarizes information for the event attributes in the event log
 */
public class EventAttributeSummaryController {

    private void initializeEventAttributeSummaryStorage(EventAttributeCollectorResult result, XLog log) {
        XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
        XAttributeInfo eventAttributeInfo = logInfo.getEventAttributeInfo();
        Collection<XAttribute> eventAttributes = eventAttributeInfo.getAttributes();
        for (XAttribute attr : eventAttributes) {
            initializeEventAttributeSummaryStorage(result, attr);
        }
    }

    public EventAttributeCollectorResult computeEventAttributeSummary(XLog log) {
        EventAttributeCollectorResult result = new EventAttributeCollectorResult();
        this.initializeEventAttributeSummaryStorage(result, log);

        for (XTrace trace : log) {
            for (XEvent event : trace) {
                for (XAttribute attribute : event.getAttributes().values()) {
                    this.addAttributeValue(result, attribute);
                }
            }
        }

        for (EventAttributeSummary<?,?> summary : result.getAttributeValues().values()) {
            summary.summarize();
        }

        return result;
    }

    public void initializeEventAttributeSummaryStorage(EventAttributeCollectorResult result, XAttribute attribute) {
        result.getAttributeValues()
                .put(attribute.getKey(), EventAttributeSummaryFactory.getEventAttributeSummary(attribute));
    }

    public void addAttributeValue(EventAttributeCollectorResult result, XAttribute attribute) {
        EventAttributeSummary<?,?> summary = result.getAttributeValues().get(attribute.getKey());
        summary.addValue(attribute);
    }
}
