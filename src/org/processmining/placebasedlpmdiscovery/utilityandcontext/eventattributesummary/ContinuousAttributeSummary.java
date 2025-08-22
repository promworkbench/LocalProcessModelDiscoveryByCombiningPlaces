package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeContinuousImpl;

import java.util.HashMap;


public class ContinuousAttributeSummary extends RangeAttributeSummary<Double, XAttributeContinuousImpl> {

    public ContinuousAttributeSummary(String key, boolean completeList) {
        super(key, completeList);
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
    protected void addValueInSummary(XAttribute attribute) {
        double value = extractAttributeValue(attribute);
        // count
        int count = (int) this.representationFeatures.getOrDefault(AttributeSummary.COUNT, 0);
        this.representationFeatures.put(AttributeSummary.COUNT, ++count);

        // min
        double min = (double) this.representationFeatures.getOrDefault(AttributeSummary.MIN, Double.MAX_VALUE);
        this.representationFeatures.put(AttributeSummary.MIN, Math.min(min, value));

        // max
        double max = (double) this.representationFeatures.getOrDefault(AttributeSummary.MAX, Double.MIN_VALUE);
        this.representationFeatures.put(AttributeSummary.MAX, Math.max(max, value));

        // sum
        double sum = (double) this.representationFeatures.getOrDefault(AttributeSummary.SUM, 0.0);
        this.representationFeatures.put(AttributeSummary.SUM, sum += value);

        // mean
        this.representationFeatures.put(AttributeSummary.MEAN, sum / count);

        // median (running median algorithm)
        double median = (double) this.representationFeatures.getOrDefault(AttributeSummary.MEDIAN, 0.0);
        this.representationFeatures.put(AttributeSummary.MEDIAN, median + (value - median) / count);
    }

    @Override
    protected Double extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    protected void initializeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put(AttributeSummary.MIN, Double.MAX_VALUE);
        this.representationFeatures.put(AttributeSummary.MAX, Double.MIN_VALUE);
        this.representationFeatures.put(AttributeSummary.MEAN, 0.0);
        this.representationFeatures.put(AttributeSummary.SUM, 0.0);
        this.representationFeatures.put(AttributeSummary.MEDIAN, 0.0);
        this.representationFeatures.put(AttributeSummary.COUNT, 0);
    }

    @Override
    protected void computeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put(AttributeSummary.MIN, this.values.stream().min(Double::compareTo).orElse(Double.NaN));
        this.representationFeatures.put(AttributeSummary.MAX, this.values.stream().max(Double::compareTo).orElse(Double.NaN));
        this.representationFeatures.put(AttributeSummary.MEAN, this.values.stream().mapToDouble(v -> v).average().orElse(Double.NaN));
        this.representationFeatures.put(AttributeSummary.SUM, this.values.stream().mapToDouble(v -> v).sum());
        this.representationFeatures.put(AttributeSummary.COUNT, this.values.size());

        double median = this.values.size() % 2 == 0 ?
                this.values.stream().mapToDouble(v -> v).sorted().skip(this.values.size() / 2 - 1).limit(2).average().orElse(Double.NaN) :
                this.values.stream().mapToDouble(v -> v).sorted().skip(this.values.size() / 2).findFirst().orElse(Double.NaN);
        this.representationFeatures.put(AttributeSummary.MEDIAN, median);
    }
}
