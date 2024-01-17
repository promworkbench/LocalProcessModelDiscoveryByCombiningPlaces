package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AttributeSummary<T, C extends XAttribute> {

    protected Class<C> attributeClass;

    protected String key;
    protected List<T> values;

    protected Map<String, Number> representationFeatures;
    private boolean changeDetected;

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
            this.changeDetected = true;
        } else {
            throw new IllegalArgumentException("The provided attribute is of type: " + attribute.getClass() +
                    " while the expected type is: " + attributeClass);
        }
    }

    protected abstract T extractAttributeValue(XAttribute attribute);

    public abstract void summarize();

    protected abstract void computeRepresentationFeatures();

    protected abstract void computeRepresentationFeaturesIfEmpty();

    public Map<String, Number> getRepresentationFeatures() {
        // if there is no values return some default representation features
        if (this.values.isEmpty()) {
            this.computeRepresentationFeaturesIfEmpty();
        }

        // if first computation or there is a change
        if (this.representationFeatures == null || this.changeDetected) {
            this.computeRepresentationFeatures(); // compute representation features
            this.changeDetected = false; // until something is added changeDetected is false
        }
        return this.representationFeatures;
    }

    public abstract boolean acceptValue(XAttribute attribute);
}
