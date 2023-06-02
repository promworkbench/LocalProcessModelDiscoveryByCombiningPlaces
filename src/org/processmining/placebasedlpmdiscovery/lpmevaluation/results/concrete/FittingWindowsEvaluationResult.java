package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Objects;

public class FittingWindowsEvaluationResult extends SimpleEvaluationResult {

    private static final long serialVersionUID = -7955265017819801410L;

    private final int windowSize;
    private int count; // number of fitting windows
    private double weightedCount; // longer matching have larger weight
    private int total; // total number of windows in the log

    private int coveredWindowsHash;

    private double normalizedResult;


    public FittingWindowsEvaluationResult(LocalProcessModel lpm, int windowSize) {
        super(lpm, LPMEvaluationResultId.FittingWindowsEvaluationResult);
        this.windowSize = windowSize;
        this.count = 0;
        this.total = 0;
        this.coveredWindowsHash = 0;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void addCoveredWindowLabel(int windowInd) {
        this.coveredWindowsHash = Objects.hash(this.coveredWindowsHash, windowInd);
    }

    public void updateCount(int count) {
        this.count += count;
    }

    public void updateWeightedCount(double weightedCount) {
        this.weightedCount += weightedCount;
    }

    public void updateTotal(int count) {
        this.total += count;
    }

    public int getCoveredWindowsHash() {
        return this.coveredWindowsHash;
    }

    private double getFittingWindowsScore() {
        if (this.total == 0)
            return -1;
        return this.count * 1.0 / this.total;
    }

    private double getWeightedFittingWindowsScore() {
        if (this.total == 0)
            return -1;
        return this.weightedCount / this.total;
    }

    public void normalizeResult(double max, double min) {
        this.normalizedResult = (this.getResult() - min) / (max - min);
    }

    @Override
    public double getResult() {
        return getWeightedFittingWindowsScore();
    }

    @Override
    public double getNormalizedResult() {
        return normalizedResult;
    }

    @Override
    public FittingWindowsEvaluationResult clone() throws CloneNotSupportedException {
        FittingWindowsEvaluationResult clone = (FittingWindowsEvaluationResult) super.clone();
        clone.setCount(this.count);
        clone.setTotal(this.total);
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FittingWindowsEvaluationResult result = (FittingWindowsEvaluationResult) o;
        return windowSize == result.windowSize &&
                count == result.count &&
                total == result.total;
    }

    @Override
    public int hashCode() {
        return Objects.hash(windowSize, count, total);
    }
}
