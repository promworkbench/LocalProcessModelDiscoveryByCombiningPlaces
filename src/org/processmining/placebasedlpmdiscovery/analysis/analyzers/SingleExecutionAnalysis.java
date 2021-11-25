package org.processmining.placebasedlpmdiscovery.analysis.analyzers;

import java.time.Duration;
import java.time.Instant;

public class SingleExecutionAnalysis {
    private Instant start;
    private Instant end;

    public void setStart(Instant start) {
        this.start = start;
    }

    public void setEnd(Instant end) {
        this.end = end;
    }

    public void start() {
        if (this.start != null)
            throw new UnsupportedOperationException("The timer has already been started");
        this.start = Instant.now();
    }

    public void stop() {
        if (this.end != null)
            throw new UnsupportedOperationException("The timer has already been ended");
        this.end = Instant.now();
    }

    public Duration getDuration() {
        if (start == null || end == null)
            return Duration.ZERO;
        return Duration.between(start, end);
    }
}