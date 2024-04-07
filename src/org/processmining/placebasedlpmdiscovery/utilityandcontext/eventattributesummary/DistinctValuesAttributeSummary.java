package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeImpl;

public abstract class DistinctValuesAttributeSummary<T, C extends XAttributeImpl> extends AttributeSummary<T, C> {

    public DistinctValuesAttributeSummary(String key, boolean completeList) {
        super(key, completeList);
    }

    @Override
    public boolean acceptValue(XAttribute attribute) {
        if (!this.completeList) {
            throw new IllegalStateException("Not all values are stored. Check AttributeSummary.completeList");
        }
        T attributeValue = this.extractAttributeValue(attribute);
        return this.values.contains(attributeValue);
    }
}
