package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

/**
 * Combination guard that negates another combination guard
 */
public class NotCombinationGuard extends SimpleCombinationGuard {

    private CombinationGuard guard;

    public NotCombinationGuard(CombinationGuard guard) {
        this.guard = guard;
    }

    @Override
    public boolean satisfies(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        return !this.guard.satisfies(lpm1, lpm2);
    }
}
