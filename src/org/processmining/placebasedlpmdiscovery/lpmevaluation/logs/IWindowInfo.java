package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.ActivityBasedTotallyOrderedEventLogTraceVariant;

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

    int getWindowCount();

    @Deprecated
    /**
     * The activity class has an integer if one wants to be more optimal in computations. So it doesn't make sense to
     * have a method that will take the int window since it would only convert each activity to the integer id. This
     * is at the moment necessary because of legacy code.
     */
    List<Integer> getIntWindow();

    ActivityBasedTotallyOrderedEventLogTraceVariant getParentTraceVariant();
}
