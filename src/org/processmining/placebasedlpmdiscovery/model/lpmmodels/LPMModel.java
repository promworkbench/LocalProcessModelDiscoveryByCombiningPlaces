package org.processmining.placebasedlpmdiscovery.model.lpmmodels;

import org.processmining.placebasedlpmdiscovery.model.lpms.LPMRepresentation;

public interface LPMModel<EXECUTABLE extends ExecutableLPMModel> extends LPMRepresentation {
    EXECUTABLE toExecutable();
}
