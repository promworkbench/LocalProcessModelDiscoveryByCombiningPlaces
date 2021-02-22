package org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.concrete;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.LPMEvaluationResultId;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.evaluation.results.SimpleEvaluationResult;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.utils.LocalProcessModelUtils;

import java.util.*;

public class PassageCoverageEvaluationResult extends SimpleEvaluationResult {

    private static final long serialVersionUID = 357586627089534388L;
    // input-output activity pairs that are covered in replay of fitting windows
    private final Set<String> coveredPassages;
    private Set<String> allPossiblePassages;

    public PassageCoverageEvaluationResult(LocalProcessModel lpm, Map<String, Integer> labelMap) {
        super(lpm);
        this.coveredPassages = new HashSet<>();
        initializePossiblePassages(labelMap);
    }

    private void initializePossiblePassages(Map<String, Integer> labelMap) {
        this.allPossiblePassages = LocalProcessModelUtils.getPossiblePassages(lpm, labelMap);
    }

    public void updatePassageCoverage(List<Integer> transitions) {
        int inActivity, outActivity = -1;
        for (Integer tr : transitions) {
            inActivity = outActivity;
            outActivity = tr;
            String key = inActivity + "-" + outActivity;
            if (inActivity >= 0 && outActivity >= 0 && this.allPossiblePassages.contains(key))
                this.coveredPassages.add(key);
        }
    }

    @Override
    public LPMEvaluationResultId getId() {
        return LPMEvaluationResultId.PassageCoverageEvaluationResult;
    }

    @Override
    public double getResult() {
        return this.coveredPassages.size() * 1.0 / this.allPossiblePassages.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PassageCoverageEvaluationResult that = (PassageCoverageEvaluationResult) o;
        return Objects.equals(coveredPassages, that.coveredPassages) &&
                Objects.equals(allPossiblePassages, that.allPossiblePassages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coveredPassages, allPossiblePassages);
    }

    public String getPassages() {
        return coveredPassages.toString();
    }
}
