package org.processmining.placebasedlpmdiscovery.lpmdistances.dependencyinjection;

import com.google.inject.AbstractModule;
import org.processmining.placebasedlpmdiscovery.lpmdistances.DefaultModelDistanceService;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceService;


public class LPMDistancesDependencyInjectionModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DataAttributeVectorExtractionDIModule());
        bind(ModelDistanceService.class).to(DefaultModelDistanceService.class);
//        install(new FactoryModuleBuilder()
//                .implement(
//                        DataAttributeModelDistance.class,
//                        EuclideanDataAttributeModelDistance.class)
//                .build(DataAttributeModelDistanceFactory.class));
    }
}
