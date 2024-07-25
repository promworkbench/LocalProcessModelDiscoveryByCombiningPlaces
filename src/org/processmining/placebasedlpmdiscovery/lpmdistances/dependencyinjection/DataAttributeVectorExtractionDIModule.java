package org.processmining.placebasedlpmdiscovery.lpmdistances.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeVectorExtractor;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeVectorExtractorFactory;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DefaultDataAttributeVectorExtractorFactory;

public class DataAttributeVectorExtractionDIModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataAttributeVectorExtractorFactory.class).to(DefaultDataAttributeVectorExtractorFactory.class);
    }
}
