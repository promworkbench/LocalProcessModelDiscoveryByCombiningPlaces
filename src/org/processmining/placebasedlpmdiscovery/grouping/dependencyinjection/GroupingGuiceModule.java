package org.processmining.placebasedlpmdiscovery.grouping.dependencyinjection;

import com.google.inject.AbstractModule;
import org.processmining.placebasedlpmdiscovery.grouping.DefaultGroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingController;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dependencyinjection.LPMDistancesDependencyInjectionModule;

public class GroupingGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new LPMDistancesDependencyInjectionModule());
        DefaultGroupingConfig defaultGroupingConfig = new DefaultGroupingConfig();
        bind(GroupingConfig.class).toInstance(defaultGroupingConfig);
        bind(ModelDistanceConfig.class).toInstance(defaultGroupingConfig.getModelDistanceConfig());
    }
}
