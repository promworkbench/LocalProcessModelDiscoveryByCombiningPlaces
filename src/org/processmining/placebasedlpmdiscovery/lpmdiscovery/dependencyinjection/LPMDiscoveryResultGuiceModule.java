package org.processmining.placebasedlpmdiscovery.lpmdiscovery.dependencyinjection;

import com.google.inject.AbstractModule;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;

public class LPMDiscoveryResultGuiceModule extends AbstractModule {
    private final LPMDiscoveryResult result;

    public LPMDiscoveryResultGuiceModule(LPMDiscoveryResult result) {
        this.result = result;
    }
    @Override
    protected void configure() {
        bind(LPMDiscoveryResult.class).toInstance(result);
    }
}
