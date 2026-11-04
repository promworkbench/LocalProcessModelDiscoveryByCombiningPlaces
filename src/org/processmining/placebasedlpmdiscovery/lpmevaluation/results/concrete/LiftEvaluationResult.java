package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.EventLogTraceVariant;

import java.util.*;

/**
 * Window-based lift: how much more often the LPM fits in a window than expected if its activities
 * occurred in windows independently of each other.
 * <p>
 * lift = P(window fits LPM) / prod_{a in activities} P(window contains a)
 */
public class LiftEvaluationResult extends SimpleEvaluationResult {

    private static final long serialVersionUID = 4186205830742963121L;

    private final Set<String> activities; // labels of the visible transitions of the LPM
    private int count; // number of fitting windows
    private int total; // total number of windows in the log
    private Map<String, Integer> windowsContainingActivity; // number of windows containing the activity

    // last counted window, to count each window once even if the LPM is reported multiple times for it
    private transient EventLogTraceVariant<?> lastCountedTraceVariant;
    private transient int lastCountedFirstPos;
    private transient int lastCountedLastPos;

    public LiftEvaluationResult(LocalProcessModel lpm) {
        super(StandardLPMEvaluationResultId.LiftEvaluationResult);
        this.activities = new HashSet<>();
        for (Transition t : lpm.getVisibleTransitions())
            this.activities.add(t.getLabel());
        this.count = 0;
        this.total = 0;
        this.windowsContainingActivity = new HashMap<>();
    }

    /**
     * Maps the lift from [0, inf) to [0, 1), with 0.5 meaning that the LPM fits as often as expected by chance.
     * Negative values (lift could not be computed) are mapped to 0.
     */
    public static double normalize(double lift) {
        if (lift < 0)
            return 0;
        return lift / (1 + lift);
    }

    public int getCount() {
        return count;
    }

    public void updateCount(int count) {
        this.count += count;
    }

    public boolean isLastCountedWindow(EventLogTraceVariant<?> traceVariant, int firstPos, int lastPos) {
        return this.lastCountedTraceVariant != null && this.lastCountedTraceVariant.equals(traceVariant)
                && this.lastCountedFirstPos == firstPos && this.lastCountedLastPos == lastPos;
    }

    public void updateLastCountedWindow(EventLogTraceVariant<?> traceVariant, int firstPos, int lastPos) {
        this.lastCountedTraceVariant = traceVariant;
        this.lastCountedFirstPos = firstPos;
        this.lastCountedLastPos = lastPos;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public Set<String> getActivities() {
        return activities;
    }

    public void setWindowsContainingActivity(Map<String, Integer> windowsContainingActivity) {
        this.windowsContainingActivity = new HashMap<>();
        for (String activity : this.activities)
            this.windowsContainingActivity.put(activity, windowsContainingActivity.getOrDefault(activity, 0));
    }

    /**
     * @return the natural logarithm of the lift, or NaN if it can not be computed
     */
    public double getLogLift() {
        if (this.total == 0 || this.count == 0 || this.activities.isEmpty())
            return Double.NaN;

        double logTotal = Math.log(this.total);
        double logLift = Math.log(this.count) - logTotal;
        for (String activity : this.activities) {
            int activityCount = this.windowsContainingActivity.getOrDefault(activity, 0);
            if (activityCount == 0)
                return Double.NaN;
            logLift -= Math.log(activityCount) - logTotal;
        }
        return logLift;
    }

    @Override
    public double getResult() {
        double logLift = getLogLift();
        if (Double.isNaN(logLift))
            return -1;
        return Math.exp(logLift);
    }

    @Override
    public double getNormalizedResult() {
        return normalize(getResult());
    }

    @Override
    public LiftEvaluationResult clone() throws CloneNotSupportedException {
        LiftEvaluationResult clone = (LiftEvaluationResult) super.clone();
        clone.windowsContainingActivity = new HashMap<>(this.windowsContainingActivity);
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LiftEvaluationResult that = (LiftEvaluationResult) o;
        return count == that.count &&
                total == that.total &&
                activities.equals(that.activities) &&
                windowsContainingActivity.equals(that.windowsContainingActivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, total, activities, windowsContainingActivity);
    }
}
