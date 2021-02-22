package org.processmining.localprocessmodeldiscoverybycombiningplaces.loganalyzer;

public class LogAnalyzerParameters {

    private int distanceLimit;

    public LogAnalyzerParameters() {
        this.distanceLimit = 7;
    }

    public int getDistanceLimit() {
        return distanceLimit;
    }

    public void setDistanceLimit(int distanceLimit) {
        this.distanceLimit = distanceLimit;
    }
}
