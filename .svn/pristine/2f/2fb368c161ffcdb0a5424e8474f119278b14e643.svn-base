package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

/**
 * Combination guard that is satisfied if all of the guards it contains are satisfied
 */
public class AndCombinationGuard extends ComplexCombinationGuard {

    public AndCombinationGuard(CombinationGuard... guards) {
        super(guards);
    }

    @Override
    public boolean satisfies(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        for (CombinationGuard guard : this.guards) {
            if (!guard.satisfies(lpm1, lpm2))
                return false;
        }
        return true;
    }
}
