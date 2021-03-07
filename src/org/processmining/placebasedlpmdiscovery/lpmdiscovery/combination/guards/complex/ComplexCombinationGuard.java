package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.complex;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuard;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.CombinationGuardAbstract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Complex guards that are built from other guards using "AND" and "OR" conditions
 */
public abstract class ComplexCombinationGuard extends CombinationGuardAbstract {

    protected List<CombinationGuard> guards;

    public ComplexCombinationGuard() {
        this.guards = new ArrayList<>();
    }

    public ComplexCombinationGuard(CombinationGuard... guards) {
        this.guards = new ArrayList<>();
        this.guards.addAll(Arrays.asList(guards));
    }

    public boolean add(CombinationGuard guard) {
        for (CombinationGuard g : this.guards) {
            if (guard.equals(g))
                return false;
        }

        this.guards.add(guard);
        return true;
    }
}
