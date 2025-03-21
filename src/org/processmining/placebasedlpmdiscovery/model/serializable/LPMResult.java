package org.processmining.placebasedlpmdiscovery.model.serializable;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryConfig;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.Exporter;

import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


public class LPMResult extends SerializableList<LocalProcessModel> implements LPMDiscoveryResult {
    private static final long serialVersionUID = 9159252267279978544L;

    private transient LPMDiscoveryInput input;
    private transient Map<String, Object> additionalResults;

    public LPMResult() {
        this.additionalResults = new HashMap<String, Object>();
    }

    // QA: How does one convert from one child to another of a superclass?
    public LPMResult(LPMDiscoveryResult result) {
        super(result.getAllLPMs());
        this.additionalResults = result.getAdditionalResults();
    }

    @Override
    public void export(Exporter<LPMDiscoveryResult> exporter, OutputStream os) {
        exporter.export(this, os);
    }

    @Override
    public Collection<LocalProcessModel> getAllLPMs() {
        return this.elements;
    }

    @Override
    public LPMDiscoveryInput getInput() {
        return this.input;
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
    public void addAdditionalResults(String key, Object additionalResult) {
        this.additionalResults.put(key, additionalResult);
    }

    @Override
    public Map<String, Object> getAdditionalResults() {
        return this.additionalResults;
    }
}
