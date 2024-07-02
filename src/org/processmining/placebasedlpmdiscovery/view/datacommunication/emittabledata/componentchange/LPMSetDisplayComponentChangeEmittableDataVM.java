package org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.componentchange;

import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;

public class LPMSetDisplayComponentChangeEmittableDataVM implements EmittableDataVM {
    private final LPMSetDisplayComponent.Type componentType;
    public LPMSetDisplayComponentChangeEmittableDataVM(LPMSetDisplayComponent.Type type) {
        this.componentType = type;
    }

    public LPMSetDisplayComponent.Type getComponentType() {
        return componentType;
    }

    @Override
    public EmittableDataTypeVM getType() {
        return EmittableDataTypeVM.LPMSetDisplayComponentChange;
    }
}
