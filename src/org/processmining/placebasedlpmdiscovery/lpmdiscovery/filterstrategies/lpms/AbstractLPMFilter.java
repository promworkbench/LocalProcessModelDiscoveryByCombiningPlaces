package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.lpms;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.HashSet;
import java.util.Set;

public abstract class AbstractLPMFilter implements LPMFilter {

    @Override
    public Set<LocalProcessModel> filter(Set<LocalProcessModel> lpms) {
        Set<LocalProcessModel> res = new HashSet<>();
        for (LocalProcessModel lpm : lpms) {
            if (shouldKeep(lpm))
                res.add(lpm);
        }
        return res;
    }
}
