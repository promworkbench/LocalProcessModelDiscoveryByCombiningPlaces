package org.processmining.placebasedlpmdiscovery.lpmdistances.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceFactory;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.EuclideanDataAttributeModelDistance;


public class LPMDistancesDependencyInjectionModule extends AbstractModule {

    private final ModelDistanceConfig distanceConfig;

    public LPMDistancesDependencyInjectionModule(ModelDistanceConfig distanceConfig) {
        this.distanceConfig = distanceConfig;
    }

    @Override
    protected void configure() {
        bind(ModelDistanceConfig.class).toInstance(distanceConfig);
        install(new FactoryModuleBuilder()
                .implement(
                        DataAttributeModelDistance.class,
                        EuclideanDataAttributeModelDistance.class)
                .build(DataAttributeModelDistanceFactory.class));
    }
}
