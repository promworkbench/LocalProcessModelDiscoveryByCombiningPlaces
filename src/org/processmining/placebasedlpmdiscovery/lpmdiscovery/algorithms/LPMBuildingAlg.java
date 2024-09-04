package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms;

import org.processmining.placebasedlpmdiscovery.model.discovery.LPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;

public interface LPMBuildingAlg<I extends LPMBuildingInput> {

    MainFPGrowthLPMTree build(I input);
}
