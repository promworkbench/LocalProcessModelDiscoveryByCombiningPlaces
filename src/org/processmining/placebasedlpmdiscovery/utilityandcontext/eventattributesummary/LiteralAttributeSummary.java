package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary;

import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.impl.XAttributeLiteralImpl;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class LiteralAttributeSummary extends DistinctValuesAttributeSummary<String, XAttributeLiteralImpl> {

    public LiteralAttributeSummary(String key) {
        super(key);
    }

    @Override
    protected void setAttributeClass() {
        this.attributeClass = XAttributeLiteralImpl.class;
    }

    @Override
    protected String extractAttributeValue(XAttribute attribute) {
        return this.attributeClass.cast(attribute).getValue();
    }

    @Override
    public void summarize() {

    }

    @Override
    protected void computeRepresentationFeatures() {
        this.representationFeatures = new HashMap<>();
//        this.representationFeatures = this.values.stream()
//                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
//                .entrySet().stream()
//                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    protected void computeRepresentationFeaturesIfEmpty() {
        computeRepresentationFeatures();
    }
}
