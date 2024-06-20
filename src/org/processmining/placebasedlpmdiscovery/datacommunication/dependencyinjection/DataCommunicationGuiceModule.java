package org.processmining.placebasedlpmdiscovery.datacommunication.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.processmining.placebasedlpmdiscovery.datacommunication.DataCommunicationController;
import org.processmining.placebasedlpmdiscovery.datacommunication.GlobalDCController;

public class DataCommunicationGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataCommunicationController.class).to(GlobalDCController.class).in(Singleton.class);
    }
}
