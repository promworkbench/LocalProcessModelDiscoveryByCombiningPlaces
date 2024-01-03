package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.EventAttributeSummary;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EventAttributeCollectorResult implements LPMCollectorResult {

    private final Map<String, EventAttributeSummary<?, ?>> attributeValues;

    public EventAttributeCollectorResult() {
        this.attributeValues = new HashMap<>();
    }

    public Map<String, EventAttributeSummary<?, ?>> getAttributeValues() {
        return attributeValues;
    }

    @Override
    public LPMCollectorResultId getId() {
        return StandardLPMCollectorResultId.EventAttributeCollectorResult;
    }

    public Collection<String> getAttributeKeys() {
        return new HashSet<>(this.attributeValues.keySet());
    }

    public EventAttributeSummary<?,?> getEventAttributeSummaryForAttributeKey(String key) {
        return this.attributeValues.get(key);
    }
}
