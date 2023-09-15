package org.processmining.placebasedlpmdiscovery.model.serializable;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.Exporter;

import java.io.OutputStream;


public class LPMResult extends SerializableList<LocalProcessModel> implements LPMDiscoveryResult {
    private static final long serialVersionUID = 9159252267279978544L;

    @Override
    public void export(Exporter<LPMDiscoveryResult> exporter, OutputStream os) {
        exporter.export(this, os);
    }
}
