package org.processmining.placebasedlpmdiscovery.lpmbuilding.storage;

import org.processmining.lpms.model.LPM;
import org.processmining.lpms.storage.LADALocalTree;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.logs.activities.Activity;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.StreamSupport;

/**
 * Stores discovered LPMs for one window.
 */
public interface WindowLPMStorage extends Iterator<LPM> { // QA: Should not be called like this, the name should
    // explain the type of storage.

    static WindowLPMStorage lada() {
        return new LADALocalTree();
    }

    /**
     * Returns the local process models in the storage.
     *
     * @return a collection of LPMs
     */
    Collection<LocalProcessModel> getLPMs();

    /**
     * Removes LPMs created for the given activity at the given position.
     *
     * @param activity - the activity for which LPMs should be removed
     * @param position - the position of the event in the parent sequence
     */
    Collection<LPM> removeLPMsFor(Activity activity, int position);

    /**
     * Adds LPMs for the given activity at the given position.
     *
     * @param activity - the activity for which LPMs should be added
     * @param position - the position of the event in the parent sequence
     * @param lpm      - the LPM to add
     * @return true if the LPM was added, false otherwise
     */
    boolean addLPMFor(Activity activity, int position, LPM lpm);

    /**
     * Adds multiple LPMs for the given activity at the given position.
     *
     * @param activity - the activity for which LPMs should be added
     * @param position - the position of the event in the parent sequence
     * @param lpms     - the LPMs to add
     * @return true if at least one LPM was added, false otherwise
     */
    default boolean addLPMsFor(Activity activity, int position, Iterable<LPM> lpms) {
        return StreamSupport.stream(lpms.spliterator(), false)
                .anyMatch(lpm -> addLPMFor(activity, position, lpm));
    }
}
