package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LiteralAttributeSummary extends DistinctValuesAttributeSummary<String, XAttributeLiteralImpl> {

    public LiteralAttributeSummary(String key, boolean completeList) {
        super(key, completeList);
    }

    @Override
    protected void setAttributeClass() {
        this.attributeClass = XAttributeLiteralImpl.class;
    }

    @Override
    protected void addValueInSummary(XAttribute attribute) {
        String value = extractAttributeValue(attribute);
        int count = (int) this.representationFeatures.getOrDefault(value, 0);
        this.representationFeatures.put(value, ++count);
    }

    @Override
    protected String extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    protected void initializeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
    }

    @Override
    protected void computeRepresentationFeatures() {
        this.representationFeatures = this.values.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
