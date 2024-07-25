package org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes;

import com.google.inject.assistedinject.Assisted;

import java.util.Collection;

public interface DataAttributeVectorExtractorFactory {

    DataAttributeVectorExtractor create(Collection<String> attributes);
}
