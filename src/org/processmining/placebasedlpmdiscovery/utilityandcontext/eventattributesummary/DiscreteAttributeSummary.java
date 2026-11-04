package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeDiscreteImpl;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.utils.QuartileTracker;

import java.util.HashMap;

public class DiscreteAttributeSummary extends RangeAttributeSummary<Long, XAttributeDiscreteImpl> {

    private final QuartileTracker quartiles = new QuartileTracker();

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

        // quartiles and median (P² estimates)
        this.quartiles.add(value, this.representationFeatures);
    }

    @Override
    protected Long extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    protected void initializeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put(AttributeSummary.MIN, Long.MAX_VALUE);
        this.representationFeatures.put(AttributeSummary.MAX, Long.MIN_VALUE);
        this.representationFeatures.put(AttributeSummary.MEAN, 0.0);
        this.representationFeatures.put(AttributeSummary.SUM, 0L);
        this.representationFeatures.put(AttributeSummary.MEDIAN, 0L);
        this.representationFeatures.put(AttributeSummary.Q1, 0L);
        this.representationFeatures.put(AttributeSummary.Q3, 0L);
        this.representationFeatures.put(AttributeSummary.COUNT, 0);
    }

    @Override
    protected void computeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put(AttributeSummary.MIN, this.values.stream().min(Long::compareTo).orElse(Long.MIN_VALUE));
        this.representationFeatures.put(AttributeSummary.MAX, this.values.stream().max(Long::compareTo).orElse(Long.MAX_VALUE));
        this.representationFeatures.put(AttributeSummary.MEAN, this.values.stream().mapToLong(v -> v).average().orElse(Long.MIN_VALUE));
        this.representationFeatures.put(AttributeSummary.SUM, this.values.stream().mapToLong(v -> v).sum());
        this.representationFeatures.put(AttributeSummary.COUNT, this.values.size());

        double[] sorted = this.values.stream().mapToDouble(v -> v).sorted().toArray();
        this.representationFeatures.put(AttributeSummary.Q1, QuartileTracker.exactQuantile(sorted, 0.25));
        this.representationFeatures.put(AttributeSummary.MEDIAN, QuartileTracker.exactQuantile(sorted, 0.5));
        this.representationFeatures.put(AttributeSummary.Q3, QuartileTracker.exactQuantile(sorted, 0.75));
    }
}
