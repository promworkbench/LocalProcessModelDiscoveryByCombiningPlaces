package org.processmining.placebasedlpmdiscovery.view.datacommunication.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.GlobalDCControllerVM;

public class DataCommunicationGuiceModuleVM extends AbstractModule {
    @Override
    protected void configure() {
        bind(DataCommunicationControllerVM.class).to(GlobalDCControllerVM.class).in(Singleton.class);
    }
}
