package org.processmining.placebasedlpmdiscovery.utils.analysis;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ExecutionAnalysisEntry {

    private SingleExecutionAnalysis current;
    private List<SingleExecutionAnalysis> executions;
    private Duration sum;
    private Duration min;
    private Duration max;
    private Duration avg;

    public ExecutionAnalysisEntry() {
        this.executions = new ArrayList<>();
    }

    public void executionStarted() {
        if (this.current != null)
            return;
        this.current = new SingleExecutionAnalysis();
        this.current.setStart(Instant.now());
    }

    public void executionStopped() {
        if (this.current == null)
            throw new UnsupportedOperationException("The execution hasn't been started yet.");
        this.current.setEnd(Instant.now());
        this.executions.add(current);
        this.updateStatistics(current.getDuration());
        this.current = null;
    }

    private void updateStatistics(Duration duration) {
        this.sum = this.executions.size() == 1 ? duration : this.sum.plus(duration);
        this.avg = this.sum.dividedBy(this.executions.size());
        if (this.executions.size() == 1 || this.min.compareTo(duration) > 0)
            this.min = duration;
        if (this.executions.size() == 1 || this.max.compareTo(duration) < 0)
            this.max = duration;
    }

    public Duration getSum() {
        return sum;
    }

    public Duration getMin() {
        return min;
    }

    public Duration getMax() {
        return max;
    }

    public Duration getAvg() {
        return avg;
    }

    public int getExecutionCount() {
        return this.executions.size();
    }
}