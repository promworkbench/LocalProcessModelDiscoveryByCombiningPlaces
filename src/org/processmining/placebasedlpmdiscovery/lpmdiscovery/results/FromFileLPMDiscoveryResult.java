package org.processmining.placebasedlpmdiscovery.lpmdiscovery.results;

import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryConfig;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.Exporter;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.ImporterFactory;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.JsonImporter;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

public class FromFileLPMDiscoveryResult implements LPMDiscoveryResult {

    private final LPMResult lpmResult;

    public FromFileLPMDiscoveryResult(String filePath) throws IOException {
        JsonImporter<LPMResult> importer = ImporterFactory.createLPMDiscoveryResultJsonImporter();
        this.lpmResult = importer.read(LPMResult.class, Files.newInputStream(Paths.get(filePath)));
    }

    @Override
    public Collection<LocalProcessModel> getAllLPMs() {
        return this.lpmResult.getAllLPMs();
    }

    @Override
    public LPMDiscoveryInput getInput() {
        return this.lpmResult.getInput();
    }

    @Override
    public void setInput(LPMDiscoveryInput input) {
        this.lpmResult.setInput(input);
    }

    @Override
    public LPMDiscoveryConfig getConfig() {
        return this.lpmResult.getConfig();
    }

    @Override
    public void keep(int lpmCount) {
        this.lpmResult.keep(lpmCount);
    }

    @Override
    public void export(Exporter<LPMDiscoveryResult> exporter, OutputStream os) {
        this.lpmResult.export(exporter, os);
    }
}
