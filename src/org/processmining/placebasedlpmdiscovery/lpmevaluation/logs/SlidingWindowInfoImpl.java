package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.ActivityCache;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.ActivityBasedTotallyOrderedEventLogTraceVariant;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SlidingWindowInfoImpl implements SlidingWindowInfo {
    private final ArrayList<Activity> window;
    private final int windowCount;
    private final int startPos; // inclusive
    private final int endPos; // inclusive
    private final ActivityBasedTotallyOrderedEventLogTraceVariant traceVariant;
    private final Activity addedActivity;
    private final Activity removedActivity;

    public SlidingWindowInfoImpl(ArrayList<Activity> window, int windowCount, int startPos, int endPos,
                                 ActivityBasedTotallyOrderedEventLogTraceVariant traceVariant,
                                 Activity addedActivity, Activity removedActivity) {
        this.window = window;
        this.windowCount = windowCount;
        this.startPos = startPos;
        this.endPos = endPos;
        this.traceVariant = traceVariant;
        this.addedActivity = addedActivity;
        this.removedActivity = removedActivity;
    }

    public List<Activity> getWindow() {
        return this.window;
    }

    @Override
    public Activity getAddedActivity() {
        return this.addedActivity;
    }

    @Override
    public Activity getRemovedActivity() {
        return this.removedActivity;
    }

    /**
     * Returns the start position of the window in the trace
     * @return startPos - the start position of the window in the trace
     */
    public int getStartPos() {
        return startPos;
    }

    /**
     * Returns the end position of the window in the trace
     * @return endPos - the end position of the window in the trace
     */
    public int getEndPos() {
        return endPos;
    }

    @Override
    public boolean hasPreviousWindow() {
        return startPos + endPos != 0;
    }

    public int getWindowCount() {
        return windowCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlidingWindowInfoImpl that = (SlidingWindowInfoImpl) o;
        return windowCount == that.windowCount && startPos == that.startPos && endPos == that.endPos && Objects.equals(window, that.window);
    }

    @Override
    public int hashCode() {
        return Objects.hash(window, windowCount, startPos, endPos);
    }

    @Override
    public String toString() {
        return "WindowInfo{" +
                "window=" + window +
                ", windowCount=" + windowCount +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                '}';
    }

    public List<Integer> getIntWindow() {
        return this.window.stream().map(a -> ActivityCache.getInstance().getIntForActivityId(a.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public ActivityBasedTotallyOrderedEventLogTraceVariant getParentTraceVariant() {
        return this.traceVariant;
    }
}
