package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeBooleanImpl;

public class BooleanEventAttributeSummary extends DistinctValuesEventAttributeSummary<Boolean, XAttributeBooleanImpl> {
    public BooleanEventAttributeSummary(String key) {
        super(key);
    }

    @Override
    protected void setAttributeClass() {
        this.attributeClass = XAttributeBooleanImpl.class;
    }

    @Override
    protected Boolean extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    public void summarize() {

    }
}
