package org.processmining.placebasedlpmdiscovery.lpmdistances.dependencyinjection;

import com.google.inject.AbstractModule;


public class LPMDistancesDependencyInjectionModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DataAttributeVectorExtractionDIModule());
//        install(new FactoryModuleBuilder()
//                .implement(
//                        DataAttributeModelDistance.class,
//                        EuclideanDataAttributeModelDistance.class)
//                .build(DataAttributeModelDistanceFactory.class));
    }
}
