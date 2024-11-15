package org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.parameters;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters.LPMBuildingParameters;

public interface LPMDiscoveryParameters {
    long getTimeLimit();

    LPMBuildingParameters getLPMBuildingParameters();

    int getLpmCount();
}
