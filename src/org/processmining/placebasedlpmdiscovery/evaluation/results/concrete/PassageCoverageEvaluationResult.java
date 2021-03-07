package org.processmining.placebasedlpmdiscovery.evaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.evaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.evaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

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
        for (int i = 0; i < transitions.size(); ++i) {
            inActivity = outActivity;
            outActivity = transitions.get(i);
            String key = inActivity + "-" + outActivity;
            if (inActivity > 0 && outActivity > 0 && this.allPossiblePassages.contains(key)) {
                this.coveredPassages.add(key);
            } else if (!this.allPossiblePassages.contains(key) && i < transitions.size() - 1) {
                key = inActivity + "-" + transitions.get(i + 1);
                if (this.allPossiblePassages.contains(key))
                    this.coveredPassages.add(key);
            }
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
    public double getNormalizedResult() {
        return getResult();
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
