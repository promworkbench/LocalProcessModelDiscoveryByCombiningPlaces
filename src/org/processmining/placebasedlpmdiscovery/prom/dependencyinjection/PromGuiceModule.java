package org.processmining.placebasedlpmdiscovery.prom.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.dependencyinjection.LPMDiscoveryGuiceModule;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.dependencyinjection.PromViewGuiceModule;

public class PromGuiceModule extends AbstractModule {
    private final PluginContext promContext;

    public PromGuiceModule(PluginContext promContext) {
        this.promContext = promContext;
    }

    @Provides
    PluginContext providePromContext() {
        return this.promContext;
    }

    @Override
    protected void configure() {
        install(new PromViewGuiceModule());
    }
}
