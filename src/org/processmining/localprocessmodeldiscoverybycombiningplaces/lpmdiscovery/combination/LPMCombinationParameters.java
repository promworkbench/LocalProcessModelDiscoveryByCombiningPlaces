package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.combination;

public class LPMCombinationParameters {

    private int minNumPlaces;
    private int maxNumPlaces;
    private int lpmProximity;
    private int minNumTransitions;
    private int maxNumTransitions;

    public LPMCombinationParameters() {
        this.minNumPlaces = 2;
        this.maxNumPlaces = 4;
        this.minNumTransitions = 3;
        this.maxNumTransitions = 6;
        this.lpmProximity = 7;
    }

    public void setMinNumPlaces(int minNumPlaces) {
        this.minNumPlaces = minNumPlaces;
    }

    public void setMaxNumPlaces(int maxNumPlaces) {
        this.maxNumPlaces = maxNumPlaces;
    }

    public int getMinNumPlaces() {
        return minNumPlaces;
    }

    public int getMaxNumPlaces() {
        return maxNumPlaces;
    }

    public void setLpmProximity(int lpmProximity) {
        this.lpmProximity = lpmProximity;
    }

    public int getLpmProximity() {
        return lpmProximity;
    }

    public int getMinNumTransitions() {
        return minNumTransitions;
    }

    public void setMinNumTransitions(int minNumTransitions) {
        this.minNumTransitions = minNumTransitions;
    }

    public int getMaxNumTransitions() {
        return maxNumTransitions;
    }

    public void setMaxNumTransitions(int maxNumTransitions) {
        this.maxNumTransitions = maxNumTransitions;
    }
}
