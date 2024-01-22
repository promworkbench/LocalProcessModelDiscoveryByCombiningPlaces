package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;

import java.util.HashMap;

public class DiscreteAttributeSummary extends RangeAttributeSummary<Long, XAttributeDiscreteImpl> {

    public DiscreteAttributeSummary(String key, boolean completeList) {
        super(key, completeList);
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
    protected void addValueInSummary(XAttribute attribute) {
        long value = extractAttributeValue(attribute);
        // count
        int count = (int) this.representationFeatures.getOrDefault(AttributeSummary.COUNT, 0);
        this.representationFeatures.put(AttributeSummary.COUNT, ++count);

        // min
        long min = (long) this.representationFeatures.getOrDefault(AttributeSummary.MIN, Long.MAX_VALUE);
        this.representationFeatures.put(AttributeSummary.MIN, Math.min(min, value));

        // max
        long max = (long) this.representationFeatures.getOrDefault(AttributeSummary.MAX, Long.MIN_VALUE);
        this.representationFeatures.put(AttributeSummary.MAX, Math.max(max, value));

        // sum
        long sum = (long) this.representationFeatures.getOrDefault(AttributeSummary.SUM, 0L);
        this.representationFeatures.put(AttributeSummary.SUM, sum += value);

        // mean
        this.representationFeatures.put(AttributeSummary.MEAN, sum / count);

        // median (running median algorithm)
        long median = (long) this.representationFeatures.getOrDefault(AttributeSummary.MEDIAN, 0L);
        this.representationFeatures.put(AttributeSummary.MEDIAN, median + (value - median) / count);
    }

    @Override
    protected Long extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
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
