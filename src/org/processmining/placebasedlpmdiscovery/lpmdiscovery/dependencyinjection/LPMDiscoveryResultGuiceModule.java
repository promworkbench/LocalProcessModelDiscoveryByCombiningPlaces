package org.processmining.placebasedlpmdiscovery.lpmdiscovery.dependencyinjection;

import com.google.inject.AbstractModule;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;

public class LPMDiscoveryResultGuiceModule extends AbstractModule {
    private final LPMDiscoveryResult result;

    public LPMDiscoveryResultGuiceModule(LPMDiscoveryResult result) {
        this.result = result;
    }
    @Override
    protected void configure() {
        install(new InputModule(this.result.getInput().getLog().getOriginalLog()));
        bind(LPMDiscoveryResult.class).toInstance(result);
    }
}
