package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

/**
 * Class that guards if a combination can be made
 */
public interface CombinationGuard {

    /**
     * Checks if the two lpms satisfy the guard condition
     *
     * @param lpm1: the first local process model that we need to combine
     * @param lpm2: the first local process model that we need to combine
     * @return whether the guard is satisfied
     */
    boolean satisfies(LocalProcessModel lpm1, LocalProcessModel lpm2);
}
