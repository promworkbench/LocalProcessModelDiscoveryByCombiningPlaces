package org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs;

import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;

public class WindowBasedLPMBuildingInput implements LPMBuildingInput {
    private final EventLog eventLog;

    public WindowBasedLPMBuildingInput(EventLog eventLog) {
        this.eventLog = eventLog;
    }

    @Override
    public EventLog getEventLog() {
        return this.eventLog;
    }
}
