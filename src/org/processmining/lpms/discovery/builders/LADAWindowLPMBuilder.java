package org.processmining.lpms.discovery.builders;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.SlidingWindowInfo;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;

/**
 * Builds LPMs for a single window.
 */
public interface LADAWindowLPMBuilder {

    static LADAWindowLPMBuilder getInstance() {
        return treeBased();
    }

    static LADAWindowLPMBuilder placeBased(PlacesProvider placesProvider) {
        return new PBLADAWindowLPMBuilder(placesProvider);
    }

    static LADAWindowLPMBuilder treeBased() {
        return new PTLADAWindowLPMBuilder();
    }

    /**
     * Builds LPMs for a single window, but if possible, reuses overlapping LPMs from the result of the previous window.
     * @param windowInfo - the window info for which we want to build LPMs
     * @param prevWindowResult -  the previous window result to reuse overlapping LPMs
     * @return a window storage that contains LPMs for the provided window
     */
    WindowLPMStorage build(SlidingWindowInfo windowInfo, WindowLPMStorage prevWindowResult);
}
