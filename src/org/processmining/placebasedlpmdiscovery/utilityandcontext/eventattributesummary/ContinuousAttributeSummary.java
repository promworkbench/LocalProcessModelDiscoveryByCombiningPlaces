package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeContinuousImpl;

import java.util.HashMap;


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
        this.setMinValue(this.values.stream().min(Double::compare).orElse(Double.MIN_VALUE));
        this.setMaxValue(this.values.stream().max(Double::compare).orElse(Double.MAX_VALUE));
    }

    @Override
    protected void computeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put("Min", this.values.stream().min(Double::compareTo).orElse(Double.NaN));
        this.representationFeatures.put("Max", this.values.stream().max(Double::compareTo).orElse(Double.NaN));
        this.representationFeatures.put("Mean", this.values.stream().mapToDouble(v -> v).average().orElse(Double.NaN));
        this.representationFeatures.put("Sum", this.values.stream().mapToDouble(v -> v).sum());

        double median = this.values.size() % 2 == 0 ?
                this.values.stream().mapToDouble(v -> v).sorted().skip(this.values.size() / 2 - 1).limit(2).average().orElse(Double.NaN) :
                this.values.stream().mapToDouble(v -> v).sorted().skip(this.values.size() / 2).findFirst().orElse(Double.NaN);
        this.representationFeatures.put("Median", median);
    }

    @Override
    protected void computeRepresentationFeaturesIfEmpty() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put("Min", Double.MIN_VALUE);
        this.representationFeatures.put("Max", Double.MIN_VALUE);
        this.representationFeatures.put("Mean", Double.MIN_VALUE);
        this.representationFeatures.put("Sum", Double.MIN_VALUE);
        this.representationFeatures.put("Median", Double.MIN_VALUE);
    }
}
