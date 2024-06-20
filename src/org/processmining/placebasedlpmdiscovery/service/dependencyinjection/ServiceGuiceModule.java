package org.processmining.placebasedlpmdiscovery.service.dependencyinjection;

import com.google.inject.AbstractModule;
import org.processmining.placebasedlpmdiscovery.datacommunication.dependencyinjection.DataCommunicationGuiceModule;
import org.processmining.placebasedlpmdiscovery.service.lpmdiscovery.DefaultLPMDiscoveryService;
import org.processmining.placebasedlpmdiscovery.service.lpmdiscovery.LPMDiscoveryService;
import org.processmining.placebasedlpmdiscovery.service.lpms.DeafaultLPMSetService;
import org.processmining.placebasedlpmdiscovery.service.lpms.LPMSetService;

public class ServiceGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new DataCommunicationGuiceModule());

        bind(LPMDiscoveryService.class).to(DefaultLPMDiscoveryService.class);
        bind(LPMSetService.class).to(DeafaultLPMSetService.class);
    }
}
