package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.impl.XAttributeImpl;

public abstract class DistinctValuesEventAttributeSummary<T, C extends XAttributeImpl> extends EventAttributeSummary<T, C> {

    public DistinctValuesEventAttributeSummary(String key) {
        super(key);
    }
}
