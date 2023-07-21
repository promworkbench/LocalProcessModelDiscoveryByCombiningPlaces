package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.util.*;

// TODO: Evaluation results should be different from evaluation result calculator.. Maybe?
public class PassageCoverageEvaluationResult extends SimpleEvaluationResult {

    private static final long serialVersionUID = 357586627089534388L;
    // input-output activity pairs that are covered in replay of fitting windows
    private final Set<String> coveredPassages;
    private Set<String> allPossiblePassages;

    public PassageCoverageEvaluationResult(LocalProcessModel lpm) {
        super(lpm, LPMEvaluationResultId.PassageCoverageEvaluationResult);
        this.coveredPassages = new HashSet<>();
        initializePossiblePassages();
    }

    private void initializePossiblePassages() {
        this.allPossiblePassages = LocalProcessModelUtils.getPossiblePassages(lpm);
    }

    public void updatePassageCoverage(Set<Pair<Integer, Integer>> passages) {
        passages.forEach(passage -> this.coveredPassages.add(passage.getKey() + "-" + passage.getValue()));
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
