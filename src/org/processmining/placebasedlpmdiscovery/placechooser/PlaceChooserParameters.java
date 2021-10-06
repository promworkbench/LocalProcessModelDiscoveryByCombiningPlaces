package org.processmining.placebasedlpmdiscovery.placechooser;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PlaceChooserParameters {

    private int placeLimit;
    private int averagePlaceDegree;
    private Set<String> chosenActivities;
    private int followRelationsLimit;
    private double coveredPassagesThreshold;

    public PlaceChooserParameters(XLog log) {
        this.placeLimit = 50;
        this.averagePlaceDegree = Integer.MAX_VALUE;
        this.chosenActivities = new HashSet<>(LogUtils.getActivitiesFromLog(log));
        this.coveredPassagesThreshold = 0.3;
    }

    public int getPlaceLimit() {
        return placeLimit;
    }

    public void setPlaceLimit(int placeLimit) {
        this.placeLimit = placeLimit;
    }

    public Set<String> getChosenActivities() {
        return chosenActivities;
    }

    public void setChosenActivities(Collection<String> chosenActivities) {
        this.chosenActivities = new HashSet<>(chosenActivities);
    }

    public int getFollowRelationsLimit() {
        return followRelationsLimit;
    }

    public void setFollowRelationsLimit(int followRelationsLimit) {
        this.followRelationsLimit = followRelationsLimit;
    }

    public double getCoveredPassagesThreshold() {
        return coveredPassagesThreshold;
    }

    public void setCoveredPassagesThreshold(double coveredPassagesThreshold) {
        this.coveredPassagesThreshold = coveredPassagesThreshold;
    }

    public int getAveragePlaceDegree() {
        return averagePlaceDegree;
    }

    public void setAveragePlaceDegree(int averagePlaceDegree) {
        this.averagePlaceDegree = averagePlaceDegree;
    }
}
