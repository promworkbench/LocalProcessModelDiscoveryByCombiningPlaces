package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeImpl;

import java.util.ArrayList;
import java.util.List;

public abstract class AttributeSummary<T, C extends XAttributeImpl> {

    protected Class<C> attributeClass;

    protected String key;
    protected List<T> values;

    public AttributeSummary(String key) {
        this.key = key;
        this.setAttributeClass();
        this.values = new ArrayList<>();
    }

    public String getKey() {
        return this.key;
    }

    public List<T> getValues() {
        return this.values;
    }

    protected abstract void setAttributeClass();

    public void addValue(XAttribute attribute) {
        if (attributeClass.isInstance(attribute)) {
            this.values.add(extractAttributeValue(attribute));
        } else {
            throw new IllegalArgumentException("The provided attribute is of type: " + attribute.getClass() +
                    " while the expected type is: " + attributeClass);
        }
    }

    protected abstract T extractAttributeValue(XAttribute attribute);

    public abstract void summarize();

    public abstract boolean acceptValue(XAttribute attribute);
}
