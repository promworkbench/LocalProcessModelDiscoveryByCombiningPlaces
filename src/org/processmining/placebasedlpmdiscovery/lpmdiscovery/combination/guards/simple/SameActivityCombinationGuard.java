package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple;


import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.Collections;

/**
 * Checks whether the local process model and the place have at least one same activity
 */
public class SameActivityCombinationGuard extends SimpleCombinationGuard {

    @Override
    public boolean satisfies(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        return !Collections.disjoint(lpm1.getTransitions(), lpm2.getTransitions())
                && !lpm1.containsLPM(lpm2) && !lpm2.containsLPM(lpm1);
    }
}
