package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.List;

public interface IWindowInfo {

    List<Activity> getWindow();

    /**
     * Returns the start position of the window in the trace (inclusive)
     * @return startPos - the start position of the window in the trace
     */
    int getStartPos();

    /**
     * Returns the end position of the window in the trace (inclusive)
     * @return endPos - the end position of the window in the trace
     */
    int getEndPos();

    boolean hasPreviousWindow();
}
