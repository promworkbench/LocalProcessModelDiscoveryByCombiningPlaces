package org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.LPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMBuildingInput;

public interface LPMBuildingAlg<I extends LPMBuildingInput> {

    LPMBuildingResult build(I input);
}
