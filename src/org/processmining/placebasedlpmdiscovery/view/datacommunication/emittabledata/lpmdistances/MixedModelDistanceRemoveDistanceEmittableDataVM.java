package org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.lpmdistances;

import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;

public class MixedModelDistanceRemoveDistanceEmittableDataVM implements EmittableDataVM {
    private final String key;
    private final int index;

    public MixedModelDistanceRemoveDistanceEmittableDataVM(String key, int index) {
        this.key = key;
        this.index = index;
    }

    public String getKey() {
        return key;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public EmittableDataTypeVM getType() {
        return EmittableDataTypeVM.MixedModelDistanceRemoveDistanceVM;
    }

    @Override
    public String getTopic() {
        return this.getType().name();
    }
}
