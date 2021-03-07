package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex.AndCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex.OrCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple.NotCombinationGuard;

/**
 * Class that contains methods for building different combination guards
 */
public class CombinationGuardBuilder {

    public static CombinationGuard and(CombinationGuard... guards) {
        AndCombinationGuard resGuard = new AndCombinationGuard();
        for (CombinationGuard guard : guards)
            resGuard.add(guard);

        return resGuard;
    }

    public static CombinationGuard or(CombinationGuard... guards) {
        OrCombinationGuard resGuard = new OrCombinationGuard();
        for (CombinationGuard guard : guards)
            resGuard.add(guard);

        return resGuard;
    }

    public static CombinationGuard not(CombinationGuard guard) {
        return new NotCombinationGuard(guard);
    }
}
