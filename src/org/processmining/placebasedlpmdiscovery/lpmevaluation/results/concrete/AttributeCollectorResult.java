package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class AttributeCollectorResult implements LPMCollectorResult {

    private final Map<String, AttributeSummary<?, ?>> attributeValues;

    public AttributeCollectorResult() {
        this.attributeValues = new HashMap<>();
    }

    public Map<String, AttributeSummary<?, ?>> getAttributeValues() {
        return attributeValues;
    }

    public Collection<String> getAttributeKeys() {
        return this.attributeValues.keySet();
    }

    public AttributeSummary<?, ?> getAttributeSummaryForAttributeKey(String key) {
        return this.attributeValues.get(key);
    }
}
