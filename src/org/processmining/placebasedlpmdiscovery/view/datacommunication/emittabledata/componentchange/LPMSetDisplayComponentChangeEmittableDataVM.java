package org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.componentchange;

import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataVM;

import java.util.Map;

public class LPMSetDisplayComponentChangeEmittableDataVM implements EmittableDataVM {
    private final LPMSetDisplayComponentType componentType;
    /**
     * Type: GroupedLPMs
     *   identifier: string which clustering to display
     */
    private final Map<String, Object> parameters;
    public LPMSetDisplayComponentChangeEmittableDataVM(LPMSetDisplayComponentType type, Map<String, Object> parameters) {
        this.componentType = type;
        this.parameters = parameters;
    }

    public LPMSetDisplayComponentType getComponentType() {
        return componentType;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public EmittableDataTypeVM getType() {
        return EmittableDataTypeVM.LPMSetDisplayComponentChangeVM;
    }

    @Override
    public String getTopic() {
        return getType().name();
    }
}
