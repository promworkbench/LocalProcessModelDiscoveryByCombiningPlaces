package org.processmining.placebasedlpmdiscovery.lpmdiscovery.dependencyinjection;

import com.google.inject.AbstractModule;
import org.processmining.placebasedlpmdiscovery.datacommunication.dependencyinjection.DataCommunicationGuiceModule;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.service.DefaultLPMDiscoveryService;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.service.LPMDiscoveryService;

public class LPMDiscoveryGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DataCommunicationGuiceModule());
        bind(LPMDiscoveryService.class).to(DefaultLPMDiscoveryService.class);
    }
}
