package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeImpl;

public abstract class DistinctValuesAttributeSummary<T, C extends XAttributeImpl> extends AttributeSummary<T, C> {

    public DistinctValuesAttributeSummary(String key) {
        super(key);
    }

    @Override
    public boolean acceptValue(XAttribute attribute) {
        T attributeValue = this.extractAttributeValue(attribute);
        return this.values.contains(attributeValue);
    }
}
