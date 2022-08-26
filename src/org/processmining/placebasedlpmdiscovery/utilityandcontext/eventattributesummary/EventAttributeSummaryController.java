package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.info.XAttributeInfo;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.*;

import java.util.*;

/**
 * Class that summarizes information for the event attributes in the event log
 */
public class EventAttributeSummaryController {

    private final Map<String, EventAttributeSummary<?, ?>> attributeValues;

    public EventAttributeSummaryController(XLog log) {
        this.attributeValues = new HashMap<>();

        XLogInfo logInfo = XLogInfoFactory.createLogInfo(log);
        XAttributeInfo eventAttributeInfo = logInfo.getEventAttributeInfo();
        Collection<XAttribute> eventAttributes = eventAttributeInfo.getAttributes();
        for (XAttribute attr : eventAttributes) {
            initializeAttributeValueStorage(attr);
        }

        for (XTrace trace : log) {
            for (XEvent event : trace) {
                for (XAttribute attribute : event.getAttributes().values()) {
                    this.addAttributeValue(attribute);
                }
            }
        }

        for (EventAttributeSummary<?,?> summary : this.attributeValues.values()) {
            summary.summarize();
        }
    }

    private void initializeAttributeValueStorage(XAttribute attribute) {
        attributeValues.put(attribute.getKey(), EventAttributeSummaryFactory.getEventAttributeSummary(attribute));
    }

    private void addAttributeValue(XAttribute attribute) {
        EventAttributeSummary<?,?> summary = this.attributeValues.get(attribute.getKey());
        summary.addValue(attribute);
    }

    public Collection<String> getAttributeKeys() {
        return new HashSet<>(this.attributeValues.keySet());
    }

    public EventAttributeSummary<?,?> getEventAttributeSummaryForAttributeKey(String key) {
        return this.attributeValues.get(key);
    }
}
