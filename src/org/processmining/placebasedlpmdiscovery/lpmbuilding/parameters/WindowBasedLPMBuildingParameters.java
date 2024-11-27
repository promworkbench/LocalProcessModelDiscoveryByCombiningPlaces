package org.processmining.placebasedlpmdiscovery.lpmbuilding.parameters;

public class WindowBasedLPMBuildingParameters implements LPMBuildingParameters {

    private final int windowSize;

    public WindowBasedLPMBuildingParameters(int windowSize) {
        this.windowSize = windowSize;
    }

    public int getWindowSize() {
        return this.windowSize;
    }
}
