package org.processmining.lpms.discovery.builders;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.IWindowInfo;

/**
 * Builds LPMs for a single window.
 */
public interface SingleWindowLPMBuilder {

    /**
     * Builds LPMs for a single window independently of the rest.
     * @param windowInfo - the window info for which we want to build LPMs
     * @return a window storage that contains LPMs for the provided window
     */
    WindowLPMStorage build(IWindowInfo windowInfo);

    /**
     * Builds LPMs for a single window, but if possible, reuses overlapping LPMs from the result of the previous window.
     * @param windowInfo - the window info for which we want to build LPMs
     * @param prevWindowResult -  the previous window result to reuse overlapping LPMs
     * @return a window storage that contains LPMs for the provided window
     */
    WindowLPMStorage build(IWindowInfo windowInfo, WindowLPMStorage prevWindowResult);
}
