package org.processmining.placebasedlpmdiscovery.grouping.dependencyinjection;

import com.google.inject.AbstractModule;
import org.processmining.placebasedlpmdiscovery.grouping.DefaultGroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;

public class GroupingGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GroupingConfig.class).to(DefaultGroupingConfig.class);
    }
}
