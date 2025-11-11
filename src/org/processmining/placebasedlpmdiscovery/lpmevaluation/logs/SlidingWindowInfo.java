package org.processmining.placebasedlpmdiscovery.lpmevaluation.logs;

import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;
import org.processmining.placebasedlpmdiscovery.model.logs.tracevariants.ActivityBasedTotallyOrderedEventLogTraceVariant;

import java.util.List;

/**
 * There are three different ways in which a current sliding window differs from the previous.
 * (1) Only adding an element: [a] -> [a b], [a b] -> [a b c]; This occurs when the sliding window capacity is not
 *  filled, i.e., at the beginning of a sequence. To see which activity has been added use
 * {@link #getAddedActivity()}.
 * (2) Removing and adding an element: [a b c] -> [b c b]; This occurs when the sliding window capacity is filled, so
 * first an element has to be removed and then added.
 * (3) Only removing an element: [b c b] -> [c b], [c b] -> [b]; This occurs when the sliding window is at the end of
 * a sequence. To see which activity has been removed use {@link #getRemovedActivity()}.
 */
public interface SlidingWindowInfo {

    List<Activity> getWindow();

    Activity getAddedActivity();

    Activity getRemovedActivity();

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
