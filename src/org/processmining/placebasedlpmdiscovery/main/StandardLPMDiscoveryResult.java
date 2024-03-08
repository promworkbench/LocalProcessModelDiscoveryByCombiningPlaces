package org.processmining.placebasedlpmdiscovery.main;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.Exporter;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;

import java.io.OutputStream;
import java.util.Collection;

public class StandardLPMDiscoveryResult implements LPMDiscoveryResult {

    private final transient MainFPGrowthLPMTree resTree;
    private Collection<LocalProcessModel> lpms;

    private transient LPMDiscoveryInput input;

    public StandardLPMDiscoveryResult(MainFPGrowthLPMTree resTree) {
        this.resTree = resTree;
    }

    /**
     * Returns the locally stored LPMs built during the LPM Discovery. If the LPMs haven't been extracted yet from
     * {@link MainFPGrowthLPMTree} they are extracted and stored locally.
     * @return locally stored LPMs
     */
    @Override
    public Collection<LocalProcessModel> getAllLPMs() {
        if (lpms == null) {
            lpms = resTree.getLPMs(Integer.MAX_VALUE);
        }
        return lpms;
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
    public void export(Exporter<LPMDiscoveryResult> exporter, OutputStream os) {
        exporter.export(this, os);
    }
}
