package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms.LPMFilterId;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class LPMFilterParameters {

    private Set<LPMFilterId> LPMFilterIds;

    private double aboveTransitionOverlappingThreshold;
    private double abovePassageCoverageThreshold;
    private double aboveWindowsEvaluationThreshold;
    private double abovePassageRepetitionThreshold;

    public LPMFilterParameters() {

        this.setDefaultFilterStrategies();

        this.abovePassageCoverageThreshold = 0;
        this.aboveWindowsEvaluationThreshold = 0;
        this.aboveTransitionOverlappingThreshold = 0;
        this.abovePassageRepetitionThreshold = 0;
    }

    private void setDefaultFilterStrategies() {
        this.LPMFilterIds = new HashSet<>();
        this.LPMFilterIds.addAll(Arrays.asList(LPMFilterId.values()));
    }

    public Set<LPMFilterId> getLPMFilterIds() {
        return LPMFilterIds;
    }

    public void setLPMFilterIds(Collection<LPMFilterId> selectedFilterStrategies) {
        this.LPMFilterIds = new HashSet<>();
        this.LPMFilterIds.addAll(selectedFilterStrategies);
    }

    public double getAboveTransitionOverlappingThreshold() {
        return aboveTransitionOverlappingThreshold;
    }

    public void setAboveTransitionOverlappingThreshold(double aboveTransitionOverlappingThreshold) {
        this.aboveTransitionOverlappingThreshold = aboveTransitionOverlappingThreshold;
    }

    public double getAbovePassageCoverageThreshold() {
        return abovePassageCoverageThreshold;
    }

    public void setAbovePassageCoverageThreshold(double abovePassageCoverageThreshold) {
        this.abovePassageCoverageThreshold = abovePassageCoverageThreshold;
    }

    public double getAboveWindowsEvaluationThreshold() {
        return aboveWindowsEvaluationThreshold;
    }

    public void setAboveWindowsEvaluationThreshold(double aboveWindowsEvaluationThreshold) {
        this.aboveWindowsEvaluationThreshold = aboveWindowsEvaluationThreshold;
    }

    public double getAbovePassageRepetitionThreshold() {
        return abovePassageRepetitionThreshold;
    }

    public void setAbovePassageRepetitionThreshold(double belowPassageRepetitionThreshold) {
        this.abovePassageRepetitionThreshold = belowPassageRepetitionThreshold;
    }
}
