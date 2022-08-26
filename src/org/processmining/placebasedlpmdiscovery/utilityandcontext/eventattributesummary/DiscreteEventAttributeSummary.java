package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;

public class DiscreteEventAttributeSummary extends RangeEventAttributeSummary<Long, XAttributeDiscreteImpl>{

    public DiscreteEventAttributeSummary(String key) {
        super(key);
    }

    @Override
    public void setMinValue(String minValue) {
        this.setMinValue(Long.parseLong(minValue));
    }

    @Override
    public void setMaxValue(String maxValue) {
        this.setMaxValue(Long.parseLong(maxValue));
    }

    @Override
    public String getStringMinValue() {
        return String.valueOf(this.getMinValue());
    }

    @Override
    public String getStringMaxValue() {
        return String.valueOf(this.getMaxValue());
    }

    @Override
    protected void setAttributeClass() {
        this.attributeClass = XAttributeDiscreteImpl.class;
    }

    @Override
    protected Long extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    public void summarize() {
        this.setMinValue(this.values.stream().min(Long::compare).orElse(Long.MAX_VALUE));
        this.setMaxValue(this.values.stream().max(Long::compare).orElse(Long.MAX_VALUE));
    }
}
