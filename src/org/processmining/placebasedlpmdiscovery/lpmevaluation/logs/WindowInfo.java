package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import java.util.ArrayList;

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

    public int getStartPos() {
        return startPos;
    }

    public int getEndPos() {
        return endPos;
    }

    public int getWindowCount() {
        return windowCount;
    }
}
