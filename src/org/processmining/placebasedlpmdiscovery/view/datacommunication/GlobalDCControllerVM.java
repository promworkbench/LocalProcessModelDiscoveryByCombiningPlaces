package org.processmining.placebasedlpmdiscovery.view.datacommunication;

import com.google.inject.Singleton;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.datalisteners.DataListenerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        Set<String> matchingKeys = this.registeredDL.keySet()
                .stream()
                .filter(s -> Pattern.compile(s).matcher(data.getTopic()).find())
                .collect(Collectors.toSet());
        Collection<DataListenerVM> registeredDLForType = matchingKeys.
                stream().map(this.registeredDL::get).flatMap(Collection::stream).collect(Collectors.toSet());
        for (DataListenerVM dl : registeredDLForType) {
            dl.receive(data);
        }
    }
}
