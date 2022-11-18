package org.processmining.placebasedlpmdiscovery.placechooser.placepredicates;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

public class NonSelfLoopPlacePredicate implements PlacePredicate {

    /**
     * Checks if all input or output transitions are self-loops
     * @param place that we want to check for filtering
     * @return true if all input or output transitions are self-loops, false otherwise
     */
    @Override
    public boolean testPlace(Place place) {
        // check if all input transitions are self-loops
        boolean allInputSelfLoops = true;
        for (Transition t : place.getInputTransitions())
            if (!place.isSelfLoop(t.getLabel())) {
                allInputSelfLoops = false;
                break;
            }

        if (allInputSelfLoops)
            return false;

        // check if all output transitions self-loops
        boolean allOutputSelfLoops = true;
        for (Transition t : place.getOutputTransitions())
            if (!place.isSelfLoop(t.getLabel())) {
                allOutputSelfLoops = false;
                break;
            }

        return !allOutputSelfLoops;
    }
}
