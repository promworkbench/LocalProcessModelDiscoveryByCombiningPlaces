package org.processmining.placebasedlpmdiscovery.model.lpmmodels;

public interface LPMModel<EXECUTABLE extends ExecutableLPMModel> {
    EXECUTABLE toExecutable();
}
