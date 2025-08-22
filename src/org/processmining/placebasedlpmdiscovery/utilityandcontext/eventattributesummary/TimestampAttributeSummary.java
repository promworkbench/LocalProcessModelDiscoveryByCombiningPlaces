package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class TimestampAttributeSummary extends RangeAttributeSummary<Date, XAttributeTimestampImpl> {

    public TimestampAttributeSummary(String key, boolean completeList) {
        super(key, completeList);
    }

    @Override
    public void setMinValue(String minValue) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.setMinValue(formatter.parse(minValue));
    }

    @Override
    public void setMaxValue(String maxValue) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        this.setMinValue(formatter.parse(maxValue));
    }

    @Override
    public String getStringMinValue() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(this.getMinValue());
    }

    @Override
    public String getStringMaxValue() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return formatter.format(this.getMaxValue());
    }

    @Override
    protected void setAttributeClass() {
        this.attributeClass = XAttributeTimestampImpl.class;
    }

    @Override
    protected void addValueInSummary(XAttribute attribute) {
        long value = extractAttributeValue(attribute).getTime();
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
    protected Date extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    protected void initializeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put(AttributeSummary.MIN, Long.MAX_VALUE);
        this.representationFeatures.put(AttributeSummary.MAX, Long.MIN_VALUE);
        this.representationFeatures.put(AttributeSummary.MEAN, 0L);
        this.representationFeatures.put(AttributeSummary.SUM, 0L);
        this.representationFeatures.put(AttributeSummary.MEDIAN, 0L);
        this.representationFeatures.put(AttributeSummary.COUNT, 0);
    }

    protected void computeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
        this.representationFeatures.put(AttributeSummary.MIN, this.values.stream().mapToLong(Date::getTime).min().orElse(Long.MIN_VALUE));
        this.representationFeatures.put(AttributeSummary.MAX, this.values.stream().mapToLong(Date::getTime).max().orElse(Long.MIN_VALUE));
        this.representationFeatures.put(AttributeSummary.MEAN, this.values.stream().mapToLong(Date::getTime).average().orElse(Long.MIN_VALUE));
        this.representationFeatures.put(AttributeSummary.SUM, this.values.stream().mapToLong(Date::getTime).sum());
        this.representationFeatures.put(AttributeSummary.COUNT, 0);

        double median = this.values.size() % 2 == 0 ?
                this.values.stream().mapToLong(Date::getTime).sorted().skip(this.values.size() / 2 - 1).limit(2).average().orElse(Long.MIN_VALUE) :
                this.values.stream().mapToLong(Date::getTime).sorted().skip(this.values.size() / 2).findFirst().orElse(Long.MIN_VALUE);
        this.representationFeatures.put(AttributeSummary.MEDIAN, median);
    }
}
