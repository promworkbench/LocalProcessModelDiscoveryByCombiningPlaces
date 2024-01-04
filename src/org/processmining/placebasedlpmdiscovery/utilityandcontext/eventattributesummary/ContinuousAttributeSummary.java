package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeContinuousImpl;


public class ContinuousAttributeSummary extends RangeAttributeSummary<Double, XAttributeContinuousImpl> {

    public ContinuousAttributeSummary(String key) {
        super(key);
    }

    @Override
    public void setMinValue(String minValue) {
        this.setMinValue(Double.parseDouble(minValue));
    }

    @Override
    public void setMaxValue(String maxValue) {
        this.setMinValue(Double.parseDouble(maxValue));
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
        this.attributeClass = XAttributeContinuousImpl.class;
    }

    @Override
    protected Double extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    public void summarize() {
        this.setMinValue(this.values.stream().min(Double::compare).orElse(-1.0));
        this.setMaxValue(this.values.stream().max(Double::compare).orElse(-1.0));
    }
}
