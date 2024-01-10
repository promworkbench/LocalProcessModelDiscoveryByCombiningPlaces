package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeBooleanImpl;

import java.util.HashMap;
import java.util.stream.Collectors;

public class BooleanAttributeSummary extends DistinctValuesAttributeSummary<Boolean, XAttributeBooleanImpl> {
    public BooleanAttributeSummary(String key) {
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

    @Override
    protected void computeRepresentationFeatures() {
        int trues = (int) this.values.stream().filter(v -> v).count();
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put("True", trues);
        this.representationFeatures.put("False", this.values.size() - trues);
    }
}
