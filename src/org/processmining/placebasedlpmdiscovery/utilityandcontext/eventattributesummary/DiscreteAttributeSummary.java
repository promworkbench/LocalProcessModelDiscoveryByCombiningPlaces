package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;

import java.util.HashMap;

public class DiscreteAttributeSummary extends RangeAttributeSummary<Long, XAttributeDiscreteImpl> {

    public DiscreteAttributeSummary(String key) {
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

    @Override
    protected void computeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put("Min", this.values.stream().min(Long::compareTo).orElse(Long.MIN_VALUE));
        this.representationFeatures.put("Max", this.values.stream().max(Long::compareTo).orElse(Long.MAX_VALUE));
        this.representationFeatures.put("Mean", this.values.stream().mapToLong(v -> v).average().orElse(Long.MIN_VALUE));
        this.representationFeatures.put("Sum", this.values.stream().mapToLong(v -> v).sum());

        double median =
                this.values.size() % 2 == 0 ?
                this.values.stream().mapToLong(v -> v).sorted().skip(this.values.size() / 2 - 1).limit(2).average().orElse(Long.MIN_VALUE) :
                this.values.stream().mapToLong(v -> v).sorted().skip(this.values.size() / 2).findFirst().orElse(Long.MIN_VALUE);
        this.representationFeatures.put("Median", median);
    }

    @Override
    protected void computeRepresentationFeaturesIfEmpty() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put("Min", Long.MIN_VALUE);
        this.representationFeatures.put("Max", Long.MIN_VALUE);
        this.representationFeatures.put("Mean", Long.MIN_VALUE);
        this.representationFeatures.put("Sum", Long.MIN_VALUE);
        this.representationFeatures.put("Median", Long.MIN_VALUE);
    }
}
