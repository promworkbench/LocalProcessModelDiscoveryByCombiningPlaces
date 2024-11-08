package org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters.LPMBuildingParameters;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.LPMBuildingResult;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.LPMBuildingInput;

public interface LPMBuildingAlg {

    LPMBuildingResult build(LPMBuildingInput input, LPMBuildingParameters parameters);
}
