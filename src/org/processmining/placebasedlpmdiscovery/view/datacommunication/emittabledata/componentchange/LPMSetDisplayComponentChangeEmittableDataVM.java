package org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.componentchange;

import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;

public class LPMSetDisplayComponentChangeEmittableDataVM implements EmittableDataVM {
    private final LPMSetDisplayComponentType componentType;
    public LPMSetDisplayComponentChangeEmittableDataVM(LPMSetDisplayComponentType type) {
        this.componentType = type;
    }

    public LPMSetDisplayComponentType getComponentType() {
        return componentType;
    }

    @Override
    public EmittableDataTypeVM getType() {
        return EmittableDataTypeVM.LPMSetDisplayComponentChangeVM;
    }
}
