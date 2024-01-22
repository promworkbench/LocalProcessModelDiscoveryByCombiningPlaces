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

    public void initializeAttributeSummaryStorage(AttributeCollectorResult result, XLog log) {
        XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
        XAttributeInfo eventAttributeInfo = logInfo.getEventAttributeInfo();
        Collection<XAttribute> eventAttributes = eventAttributeInfo.getAttributes();
        for (XAttribute attr : eventAttributes) {
            initializeAttributeSummaryStorage(result, attr);
        }
    }

    public void initializeAttributeSummaryStorage(AttributeCollectorResult result, XAttribute attribute) {
        result.getAttributeValues()
                .put(attribute.getKey(), EventAttributeSummaryFactory.getEventAttributeSummary(attribute, false));
    }

    /**
     * Adds attribute to an  {@link org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.AttributeCollectorResult}.
     * If the attribute is the first one to be added of this kind, a new summary is created using the
     * {@link org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.EventAttributeSummaryFactory#getEventAttributeSummary(XAttribute, boolean)}
     * and added to the {@link org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.AttributeCollectorResult}.
     */
    public void addAttributeValue(AttributeCollectorResult result, XAttribute attribute) {
        AttributeSummary<?,?> summary = result.getAttributeValues().get(attribute.getKey());
        if (summary == null) {
            summary = EventAttributeSummaryFactory.getEventAttributeSummary(attribute, false);
            result.getAttributeValues().put(attribute.getKey(), summary);
        }
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

        return result;
    }

    public AttributeSummary<?,?> computeEventAttributeSummary(XLog log, String attributeKey) {
        AttributeSummary<?,?> attributeSummary = null;

        for (XTrace trace : log) {
            for (XEvent event : trace) {
                if (!event.getAttributes().containsKey(attributeKey)) {
                    continue;
                }
                XAttribute attribute = event.getAttributes().get(attributeKey);

                if (attributeSummary == null) {
                    attributeSummary = EventAttributeSummaryFactory.getEventAttributeSummary(attribute, false);
                }
                attributeSummary.addValue(attribute);
            }
        }

        return attributeSummary;
    }

    public void computeAttributeSummary(AttributeCollectorResult result,
                                        XElement event) {
        for (XAttribute attribute : event.getAttributes().values()) {
            this.addAttributeValue(result, attribute);
        }
    }
}
