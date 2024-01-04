package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;

public class LiteralAttributeSummary extends DistinctValuesAttributeSummary<String, XAttributeLiteralImpl> {

    public LiteralAttributeSummary(String key) {
        super(key);
    }

    @Override
    protected void setAttributeClass() {
        this.attributeClass = XAttributeLiteralImpl.class;
    }

    @Override
    protected String extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    public void summarize() {

    }
}
