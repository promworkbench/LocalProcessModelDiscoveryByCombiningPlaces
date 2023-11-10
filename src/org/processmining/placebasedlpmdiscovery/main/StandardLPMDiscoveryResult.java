package org.processmining.placebasedlpmdiscovery.main;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.Exporter;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;

import java.io.OutputStream;
import java.util.Collection;

public class StandardLPMDiscoveryResult implements LPMDiscoveryResult {

    private final MainFPGrowthLPMTree resTree;

    public StandardLPMDiscoveryResult(MainFPGrowthLPMTree resTree) {
        this.resTree = resTree;
    }

    @Override
    public Collection<LocalProcessModel> getAllLPMs() {
        return resTree.getLPMs(Integer.MAX_VALUE);
    }

    @Override
    public void export(Exporter<LPMDiscoveryResult> exporter, OutputStream os) {
        throw new NotImplementedException("This export is still not implemented");
    }
}
