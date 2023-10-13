package org.processmining.placebasedlpmdiscovery.model.inout;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.main.MultipleLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.*;

public class TwoStandardLPMDiscoveryResults implements MultipleLPMDiscoveryResults {

    private final Map<String, LPMDiscoveryResult> resMap;

    public TwoStandardLPMDiscoveryResults(LPMDiscoveryResult res1, LPMDiscoveryResult res2) {
        this.resMap = new HashMap<>();
        this.resMap.put("Set 1", res1);
        this.resMap.put("Set 2", res2);
    }

    @Override
    public Collection<LocalProcessModel> getAllLPMs() {
        Set<LocalProcessModel> all = new HashSet<>();
        all.addAll(resMap.get("Set 1").getAllLPMs());
        all.addAll(resMap.get("Set 2").getAllLPMs());
        return all;
    }

    @Override
    public Map<String, LPMDiscoveryResult> getResults() {
        return resMap;
    }

    @Override
    public LPMDiscoveryResult getResult(String name) {
        return this.resMap.get(name);
    }
}
