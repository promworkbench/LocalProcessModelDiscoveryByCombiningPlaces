package org.processmining.placebasedlpmdiscovery.model.serializable.grouped;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class WindowCoverageLPMResultGroup extends LPMResultGroup {

    private static final long serialVersionUID = 8275177127511745744L;

    private int windowCount;
    private Set<String> activities;

    public WindowCoverageLPMResultGroup() {
        super();

        this.windowCount = 0;
        this.activities = new HashSet<>();
    }

    @Override
    public boolean shouldNotAdd(LocalProcessModel element) {
        FittingWindowsEvaluationResult fitWindowRes = element.getAdditionalInfo()
                .getEvaluationResult(
                        LPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                        FittingWindowsEvaluationResult.class);
        return this.windowCount != fitWindowRes.getCount()
                || this.commonId != fitWindowRes.getCoveredWindowsHash()
                || !this.activities.equals(element.getTransitions().stream()
                    .map(Transition::getLabel)
                    .collect(Collectors.toSet()));
    }

    @Override
    public void initializeGroup(LocalProcessModel element) {
        FittingWindowsEvaluationResult fitWindowRes = element.getAdditionalInfo()
                .getEvaluationResult(
                        LPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                        FittingWindowsEvaluationResult.class);
        this.commonId = fitWindowRes.getCoveredWindowsHash();
        this.windowCount = fitWindowRes.getCount();
        this.activities = element.getTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet());
    }
}
