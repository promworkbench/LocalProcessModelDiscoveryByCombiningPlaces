package org.processmining.placebasedlpmdiscovery.datacommunication;

import com.google.inject.Singleton;
import org.processmining.placebasedlpmdiscovery.datacommunication.datalisteners.DataListener;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableData;
import org.processmining.placebasedlpmdiscovery.datacommunication.emittabledata.EmittableDataType;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Singleton
public class GlobalDCController implements DataCommunicationController {

    private final Map<EmittableDataType, Collection<DataListener>> registeredDL;

    public GlobalDCController() {
        this.registeredDL = new HashMap<>();
    }

    public void registerDataListener(DataListener dataListener, EmittableDataType dataType) {
        Collection<DataListener> registeredDLForType = this.registeredDL.getOrDefault(dataType, new HashSet<>());
        registeredDLForType.add(dataListener);
        registeredDL.put(dataType, registeredDLForType);
    }

    public void emit(EmittableData data) {
        Collection<DataListener> registeredDLForType = this.registeredDL.getOrDefault(data.getType(), new HashSet<>());
        for (DataListener dl : registeredDLForType) {
            dl.receive(data);
        }
    }
}
