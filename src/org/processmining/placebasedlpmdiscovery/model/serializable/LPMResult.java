package org.processmining.placebasedlpmdiscovery.model.serializable;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.main.StandardLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.Exporter;

import java.util.Collection;
import java.io.OutputStream;


public class LPMResult extends SerializableList<LocalProcessModel> implements LPMDiscoveryResult {
    private static final long serialVersionUID = 9159252267279978544L;

    public LPMResult() {

    }

    // QA: How does one convert from one child to another of a superclass?
    public LPMResult(StandardLPMDiscoveryResult result) {
        super(result.getAllLPMs());
    }

    @Override
    public void export(Exporter<LPMDiscoveryResult> exporter, OutputStream os) {
        exporter.export(this, os);
    }

    @Override
    public Collection<LocalProcessModel> getAllLPMs() {
        return this.elements;
    }
}
