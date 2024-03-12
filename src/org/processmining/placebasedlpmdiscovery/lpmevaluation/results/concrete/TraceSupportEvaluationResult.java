package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TraceSupportEvaluationResult extends SimpleEvaluationResult {

    private final Map<Integer, Integer> coveredTraces;
    private int totalTraceCount;

    public TraceSupportEvaluationResult(LocalProcessModel lpm) {
        super(StandardLPMEvaluationResultId.TraceSupportEvaluationResult);
        this.coveredTraces = new HashMap<>();
    }

    public void setTotalTraceCount(int count) {
        this.totalTraceCount = count;
    }

    @Override
    public double getResult() {
        return this.coveredTraces.values().stream().mapToInt(Integer::intValue).sum() * 1.0 / totalTraceCount;
    }

    @Override
    public double getNormalizedResult() {
        return getResult();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraceSupportEvaluationResult that = (TraceSupportEvaluationResult) o;
        return totalTraceCount == that.totalTraceCount && coveredTraces.equals(that.coveredTraces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalTraceCount, coveredTraces);
    }

    public void addTraces(Integer traceVariantId, int windowMultiplicity) {
        this.coveredTraces.put(traceVariantId, windowMultiplicity);
    }
}
