package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.combination.guards.complex;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;

/**
 * Combination guard which is satisfy if at least one of the guards it contains is satisfied
 */
public class OrCombinationGuard extends ComplexCombinationGuard {

    public OrCombinationGuard(CombinationGuard... guards) {
        super(guards);
    }

    @Override
    public boolean satisfies(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        for (CombinationGuard guard : this.guards) {
            if (guard.satisfies(lpm1, lpm2))
                return true;
        }
        return false;
    }
}
