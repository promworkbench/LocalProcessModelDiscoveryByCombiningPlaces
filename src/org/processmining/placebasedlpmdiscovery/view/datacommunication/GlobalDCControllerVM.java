package org.processmining.placebasedlpmdiscovery.view.datacommunication;

import com.google.inject.Singleton;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners.DataListenerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Singleton
public class GlobalDCControllerVM implements DataCommunicationControllerVM {

    private final Map<String, Collection<DataListenerVM>> registeredDL;

    public GlobalDCControllerVM() {
        this.registeredDL = new HashMap<>();
    }

    public void registerDataListener(DataListenerVM dataListenerVM, String topic) {
        Collection<DataListenerVM> registeredDLForType = this.registeredDL.getOrDefault(topic, new HashSet<>());
        registeredDLForType.add(dataListenerVM);
        registeredDL.put(topic, registeredDLForType);
    }

    public void emit(EmittableDataVM data) {
        Collection<DataListenerVM> registeredDLForType = this.registeredDL.getOrDefault(data.getTopic(), new HashSet<>());
        for (DataListenerVM dl : registeredDLForType) {
            dl.receive(data);
        }
    }
}
