package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.*;

public class EventAttributeSummaryFactory {

    public static AttributeSummary<?,?> getEventAttributeSummary(XAttribute attribute, boolean completeList) {
        if (attribute.getClass().equals(XAttributeContinuousImpl.class)) {
            return new ContinuousAttributeSummary(attribute.getKey(), completeList);
        } else if (attribute.getClass().equals(XAttributeDiscreteImpl.class)) {
            return new DiscreteAttributeSummary(attribute.getKey(), completeList);
        } else if (attribute.getClass().equals(XAttributeLiteralImpl.class)) {
            return new LiteralAttributeSummary(attribute.getKey(), completeList);
        } else if (attribute.getClass().equals(XAttributeTimestampImpl.class)) {
            return new TimestampAttributeSummary(attribute.getKey(), completeList);
        } else if (attribute.getClass().equals(XAttributeBooleanImpl.class)) {
            return new BooleanAttributeSummary(attribute.getKey(), completeList);
        } else {
            return null;
//            throw new UnsupportedOperationException("Creating summary for the attribute type: " + attribute.getClass() + " is currently not supported");
        }
    }
}
