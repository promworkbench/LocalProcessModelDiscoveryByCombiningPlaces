package org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.windowbased;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.LPMBuildingAlg;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.LPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters.LPMBuildingParameters;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.results.LPMBuildingResult;

public class WindowBasedLPMBuildingAlg implements LPMBuildingAlg {

    private final SingleWindowLPMBuilder singleWindowLPMBuilder;

    public WindowBasedLPMBuildingAlg(SingleWindowLPMBuilder singleWindowLPMBuilder) {
        this.singleWindowLPMBuilder = singleWindowLPMBuilder;
    }

    @Override
    public LPMBuildingResult build(LPMBuildingInput input, LPMBuildingParameters parameters) {
        return null;
    }
}
