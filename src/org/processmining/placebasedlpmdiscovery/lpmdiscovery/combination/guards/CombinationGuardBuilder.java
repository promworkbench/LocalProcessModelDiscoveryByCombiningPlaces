package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex.OrCombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple.NotCombinationGuard;

/**
 * Class that contains methods for building different combination guards
 */
public class CombinationGuardBuilder {

    public static org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard and(org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard... guards) {
        CombinationGuard resGuard = new CombinationGuard();
        for (org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard guard : guards)
            resGuard.add(guard);

        return resGuard;
    }

    public static org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard or(org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard... guards) {
        OrCombinationGuard resGuard = new OrCombinationGuard();
        for (org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard guard : guards)
            resGuard.add(guard);

        return resGuard;
    }

    public static org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard not(org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard guard) {
        return new NotCombinationGuard(guard);
    }
}
