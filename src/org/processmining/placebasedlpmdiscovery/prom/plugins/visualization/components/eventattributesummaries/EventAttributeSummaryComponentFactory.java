package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.eventattributesummaries;

import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.*;

public class EventAttributeSummaryComponentFactory {
    public static EventAttributeSummaryComponent<?> getComponentForEventAttributeSummary(AttributeSummary<?, ?> attributeSummary) {
        if (attributeSummary instanceof ContinuousAttributeSummary) {
            ContinuousAttributeSummary summary = (ContinuousAttributeSummary) attributeSummary;
            return new RangeEventAttributeSummaryComponent(summary);
        } else if (attributeSummary instanceof DiscreteAttributeSummary) {
            DiscreteAttributeSummary summary = (DiscreteAttributeSummary) attributeSummary;
            return new RangeEventAttributeSummaryComponent(summary);
        } else if (attributeSummary instanceof TimestampAttributeSummary) {
            TimestampAttributeSummary summary = (TimestampAttributeSummary) attributeSummary;
            return new RangeEventAttributeSummaryComponent(summary);
        } else if (attributeSummary instanceof DistinctValuesAttributeSummary<?,?>) {
            DistinctValuesAttributeSummary<?,?> summary = (DistinctValuesAttributeSummary<?,?>) attributeSummary;
            return new DistinctValuesEventAttributeSummaryComponent(summary);
        } else {
            throw new IllegalArgumentException("Component for the type: " + attributeSummary.getClass() + " is not supported");
        }
    }
}
