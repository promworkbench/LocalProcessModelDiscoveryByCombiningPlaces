package org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes;

import com.google.inject.Inject;
import org.deckfour.xes.model.XLog;

import java.util.Collection;

public class DefaultDataAttributeVectorExtractorFactory implements DataAttributeVectorExtractorFactory {

    private final XLog log;

    @Inject
    public DefaultDataAttributeVectorExtractorFactory(XLog log) {
        this.log = log;
    }

    @Override
    public DataAttributeVectorExtractor create(Collection<String> attributes) {
        return new DataAttributeVectorExtractor(this.log, attributes);
    }
}
