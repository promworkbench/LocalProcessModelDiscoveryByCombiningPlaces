package org.processmining.placebasedlpmdiscovery.view.datacommunication;

import org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners.DataListenerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class GlobalDCControllerVM implements DataCommunicationControllerVM {

    private final Map<EmittableDataTypeVM, Collection<DataListenerVM>> registeredDL;

    public GlobalDCControllerVM() {
        this.registeredDL = new HashMap<>();
    }

    public void registerDataListener(DataListenerVM dataListenerVM, EmittableDataTypeVM dataType) {
        Collection<DataListenerVM> registeredDLForType = this.registeredDL.getOrDefault(dataType, new HashSet<>());
        registeredDLForType.add(dataListenerVM);
        registeredDL.put(dataType, registeredDLForType);
    }

    public void emit(EmittableDataVM data) {
        Collection<DataListenerVM> registeredDLForType = this.registeredDL.getOrDefault(data.getType(), new HashSet<>());
        for (DataListenerVM dl : registeredDLForType) {
            dl.receive(data);
        }
    }
}
