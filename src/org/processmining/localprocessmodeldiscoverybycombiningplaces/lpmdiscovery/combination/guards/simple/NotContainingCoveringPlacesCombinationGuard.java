package org.processmining.localprocessmodeldiscoverybycombiningplaces.lpmdiscovery.combination.guards.simple;

import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.Place;

public class NotContainingCoveringPlacesCombinationGuard extends SimpleCombinationGuard {
    @Override
    public boolean satisfies(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        for (Place p1 : lpm1.getPlaces())
            for (Place p2 : lpm2.getPlaces())
                if (p1.covers(p2) || p2.covers(p1))
                    return false;

        return true;
    }
}
