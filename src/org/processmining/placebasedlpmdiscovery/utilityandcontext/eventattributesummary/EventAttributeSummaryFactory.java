package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.*;

public class EventAttributeSummaryFactory {

    public static EventAttributeSummary<?,?> getEventAttributeSummary(XAttribute attribute) {
        if (attribute.getClass().equals(XAttributeContinuousImpl.class)) {
            return new ContinuousEventAttributeSummary(attribute.getKey());
        } else if (attribute.getClass().equals(XAttributeDiscreteImpl.class)) {
            return new DiscreteEventAttributeSummary(attribute.getKey());
        } else if (attribute.getClass().equals(XAttributeLiteralImpl.class)) {
            return new LiteralEventAttributeSummary(attribute.getKey());
        } else if (attribute.getClass().equals(XAttributeTimestampImpl.class)) {
            return new TimestampEventAttributeSummary(attribute.getKey());
        } else if (attribute.getClass().equals(XAttributeBooleanImpl.class)) {
            return new BooleanEventAttributeSummary(attribute.getKey());
        } else {
            throw new UnsupportedOperationException("Creating summary for the attribute type: " + attribute.getClass() + " is currently not supported");
        }
    }
}
