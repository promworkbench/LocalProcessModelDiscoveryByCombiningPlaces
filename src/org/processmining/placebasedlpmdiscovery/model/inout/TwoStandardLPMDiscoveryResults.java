package org.processmining.placebasedlpmdiscovery.model.inout;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.main.MultipleLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.*;

public class TwoStandardLPMDiscoveryResults implements MultipleLPMDiscoveryResults {
    private final LPMDiscoveryResult res1;
    private final LPMDiscoveryResult res2;

    public TwoStandardLPMDiscoveryResults(LPMDiscoveryResult res1, LPMDiscoveryResult res2) {
        this.res1 = res1;
        this.res2 = res2;
    }

    @Override
    public Collection<LocalProcessModel> getAllLPMs() {
        Set<LocalProcessModel> all = new HashSet<>();
        all.addAll(res1.getAllLPMs());
        all.addAll(res2.getAllLPMs());
        return all;
    }

    @Override
    public Collection<LPMDiscoveryResult> getResults() {
        return Arrays.asList(this.res1, this.res2);
    }
}
