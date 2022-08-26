package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeImpl;

import java.text.ParseException;
import java.util.Comparator;

public abstract class RangeEventAttributeSummary<T extends Comparable<T>, C extends XAttributeImpl> extends EventAttributeSummary<T, C> {

    private T minValue;
    private T maxValue;

    public RangeEventAttributeSummary(String key) {
        super(key);
    }

    public abstract void setMinValue(String minValue) throws ParseException;

    public abstract void setMaxValue(String maxValue) throws ParseException;

    public abstract String getStringMinValue();

    public abstract String getStringMaxValue();

    public void setMinValue(T minValue) {
        this.minValue = minValue;
    }

    public T getMinValue() {
        return minValue;
    }

    public void setMaxValue(T maxValue) {
        this.maxValue = maxValue;
    }

    public T getMaxValue() {
        return maxValue;
    }

    @Override
    public boolean acceptValue(XAttribute attribute) {
        T attributeValue = this.extractAttributeValue(attribute);
        return minValue.compareTo(attributeValue) < 1 && maxValue.compareTo(attributeValue) > -1;
    }
}
