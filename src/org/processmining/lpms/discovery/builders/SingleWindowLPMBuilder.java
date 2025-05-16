package org.processmining.lpms.discovery.builders;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.SlidingWindowInfo;

/**
 * Builds LPMs for a single window.
 */
public interface SingleWindowLPMBuilder {

    static SingleWindowLPMBuilder getInstance() {
        return new LADASingleWindowLPMBuilder();
    }

    /**
     * Builds LPMs for a single window, but if possible, reuses overlapping LPMs from the result of the previous window.
     * @param windowInfo - the window info for which we want to build LPMs
     * @param prevWindowResult -  the previous window result to reuse overlapping LPMs
     * @return a window storage that contains LPMs for the provided window
     */
    WindowLPMStorage build(SlidingWindowInfo windowInfo, WindowLPMStorage prevWindowResult);
}
