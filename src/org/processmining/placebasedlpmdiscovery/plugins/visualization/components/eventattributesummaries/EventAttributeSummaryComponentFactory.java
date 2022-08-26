package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.eventattributesummaries;

import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.*;

public class EventAttributeSummaryComponentFactory {
    public static EventAttributeSummaryComponent<?> getComponentForEventAttributeSummary(EventAttributeSummary<?, ?> eventAttributeSummary) {
        if (eventAttributeSummary instanceof ContinuousEventAttributeSummary) {
            ContinuousEventAttributeSummary summary = (ContinuousEventAttributeSummary) eventAttributeSummary;
            return new RangeEventAttributeSummaryComponent(summary);
        } else if (eventAttributeSummary instanceof DiscreteEventAttributeSummary) {
            DiscreteEventAttributeSummary summary = (DiscreteEventAttributeSummary) eventAttributeSummary;
            return new RangeEventAttributeSummaryComponent(summary);
        } else if (eventAttributeSummary instanceof TimestampEventAttributeSummary) {
            TimestampEventAttributeSummary summary = (TimestampEventAttributeSummary) eventAttributeSummary;
            return new RangeEventAttributeSummaryComponent(summary);
        } else if (eventAttributeSummary instanceof DistinctValuesEventAttributeSummary<?,?>) {
            DistinctValuesEventAttributeSummary<?,?> summary = (DistinctValuesEventAttributeSummary<?,?>) eventAttributeSummary;
            return new DistinctValuesEventAttributeSummaryComponent(summary);
        } else {
            throw new IllegalArgumentException("Component for the type: " + eventAttributeSummary.getClass() + " is not supported");
        }
    }
}
