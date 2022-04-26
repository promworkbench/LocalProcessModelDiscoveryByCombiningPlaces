package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeTimestampImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampEventAttributeSummary extends RangeEventAttributeSummary<Date, XAttributeTimestampImpl>{

    public TimestampEventAttributeSummary(String key) {
        super(key);
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
    protected Date extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    public void summarize() {
        Date min = this.values.get(0);
        Date max = this.values.get(0);
        for (Date date : this.values) {
            if (date.before(min))
                min = date;
            if (date.after(max))
                max = date;
        }
        this.setMinValue(min);
        this.setMaxValue(max);
    }
}
