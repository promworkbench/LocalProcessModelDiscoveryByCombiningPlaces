package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.info.XAttributeInfo;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.*;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.AttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventAttributeCollectorResult;

import java.util.*;

/**
 * Class that summarizes information for the event attributes in the event log
 */
public class AttributeSummaryController {

    private void initializeAttributeSummaryStorage(AttributeCollectorResult result, XLog log) {
        XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
        XAttributeInfo eventAttributeInfo = logInfo.getEventAttributeInfo();
        Collection<XAttribute> eventAttributes = eventAttributeInfo.getAttributes();
        for (XAttribute attr : eventAttributes) {
            initializeAttributeSummaryStorage(result, attr);
        }
    }

    public void initializeAttributeSummaryStorage(AttributeCollectorResult result, XAttribute attribute) {
        result.getAttributeValues()
                .put(attribute.getKey(), EventAttributeSummaryFactory.getEventAttributeSummary(attribute));
    }

    public void addAttributeValue(AttributeCollectorResult result, XAttribute attribute) {
        AttributeSummary<?,?> summary = result.getAttributeValues().get(attribute.getKey());
        summary.addValue(attribute);
    }

    public EventAttributeCollectorResult computeEventAttributeSummary(XLog log) {
        EventAttributeCollectorResult result = new EventAttributeCollectorResult();
        this.initializeAttributeSummaryStorage(result, log);

        for (XTrace trace : log) {
            for (XEvent event : trace) {
                for (XAttribute attribute : event.getAttributes().values()) {
                    this.addAttributeValue(result, attribute);
                }
            }
        }

        for (AttributeSummary<?,?> summary : result.getAttributeValues().values()) {
            summary.summarize();
        }

        return result;
    }

    public void computeAttributeSummary(AttributeCollectorResult result,
                                        XElement event) {
        for (XAttribute attribute : event.getAttributes().values()) {
            this.addAttributeValue(result, attribute);
        }
    }
}
