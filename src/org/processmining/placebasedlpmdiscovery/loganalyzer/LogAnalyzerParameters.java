package org.processmining.placebasedlpmdiscovery.loganalyzer;

public class LogAnalyzerParameters {

    private int distanceLimit;

    public LogAnalyzerParameters(int distanceLimit) {
        this.distanceLimit = distanceLimit;
    }

    public int getDistanceLimit() {
        return distanceLimit;
    }

    public void setDistanceLimit(int distanceLimit) {
        this.distanceLimit = distanceLimit;
    }
}
