package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeBooleanImpl;

import java.util.HashMap;

public class BooleanAttributeSummary extends DistinctValuesAttributeSummary<Boolean, XAttributeBooleanImpl> {

    private static final String TRUE = "true";
    private static final String FALSE = "false";

    public BooleanAttributeSummary(String key, boolean completeList) {
        super(key, completeList);
    }

    @Override
    protected void setAttributeClass() {
        this.attributeClass = XAttributeBooleanImpl.class;
    }

    @Override
    protected void addValueInSummary(XAttribute attribute) {
        boolean value = this.extractAttributeValue(attribute);
        String key = value ? BooleanAttributeSummary.TRUE : BooleanAttributeSummary.FALSE;
        int count = (int) this.representationFeatures.getOrDefault(key, 0);
        this.representationFeatures.put(key, ++count);
    }

    @Override
    protected Boolean extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    protected void initializeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put(BooleanAttributeSummary.TRUE, 0);
        this.representationFeatures.put(BooleanAttributeSummary.FALSE, 0);
    }

    @Override
    protected void computeRepresentationFeatures() {
        int trues = (int) this.values.stream().filter(v -> v).count();
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put(BooleanAttributeSummary.TRUE, trues);
        this.representationFeatures.put(BooleanAttributeSummary.FALSE, this.values.size() - trues);
    }
}
