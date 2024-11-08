package org.processmining.placebasedlpmdiscovery.model.inout;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryConfig;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.main.MultipleLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.Exporter;

import java.io.OutputStream;
import java.util.*;

public class TwoStandardLPMDiscoveryResults implements MultipleLPMDiscoveryResults {

    private final Map<String, LPMDiscoveryResult> resMap;
    private LPMDiscoveryInput input;

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
    public LPMDiscoveryInput getInput() {
        return input;
    }

    @Override
    public void setInput(LPMDiscoveryInput input) {
        this.input = input;
    }

    @Override
    public LPMDiscoveryConfig getConfig() {
        throw new NotImplementedException("Still not implemented.");
    }

    @Override
    public void keep(int lpmCount) {
        resMap.values().forEach(lpmRes -> lpmRes.keep(lpmCount));
    }

    @Override
    public Map<String, LPMDiscoveryResult> getResults() {
        return resMap;
    }

    @Override
    public LPMDiscoveryResult getResult(String name) {
        return this.resMap.get(name);
    }

    @Override
    public void export(Exporter<LPMDiscoveryResult> exporter, OutputStream os) {
        exporter.export(this, os);
    }
}
