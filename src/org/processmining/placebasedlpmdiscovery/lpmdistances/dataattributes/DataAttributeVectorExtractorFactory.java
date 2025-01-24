package org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes;

import org.deckfour.xes.model.XLog;

import java.util.Collection;

public interface DataAttributeVectorExtractorFactory {

    static DataAttributeVectorExtractorFactory getInstance(XLog log) {
        return new DefaultDataAttributeVectorExtractorFactory(log);
    }

    DataAttributeVectorExtractor create(Collection<String> attributes);
}
