package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import java.util.ArrayList;
import java.util.Objects;

public class WindowInfo {
    private final ArrayList<Integer> window;
    private final int windowCount;
    private final Integer traceVariantId;
    private final int startPos;
    private final int endPos;

    public WindowInfo(ArrayList<Integer> window, int windowCount, Integer traceVariantId, int startPos, int endPos) {
        this.window = window;
        this.windowCount = windowCount;
        this.traceVariantId = traceVariantId;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    public ArrayList<Integer> getWindow() {
        return window;
    }

    public Integer getTraceVariantId() {
        return traceVariantId;
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

    public int getWindowCount() {
        return windowCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WindowInfo that = (WindowInfo) o;
        return windowCount == that.windowCount && startPos == that.startPos && endPos == that.endPos && Objects.equals(window, that.window) && Objects.equals(traceVariantId, that.traceVariantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(window, windowCount, traceVariantId, startPos, endPos);
    }

    @Override
    public String toString() {
        return "WindowInfo{" +
                "window=" + window +
                ", windowCount=" + windowCount +
                ", traceVariantId=" + traceVariantId +
                ", startPos=" + startPos +
                ", endPos=" + endPos +
                '}';
    }
}
