package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

/**
 * Combination guard that is satisfied if all of the guards it contains are satisfied
 */
public class CombinationGuard extends ComplexCombinationGuard {

    public CombinationGuard(org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard... guards) {
        super(guards);
    }

    @Override
    public boolean satisfies(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        for (org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard guard : this.guards) {
            if (!guard.satisfies(lpm1, lpm2))
                return false;
        }
        return true;
    }
}
