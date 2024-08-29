package org.processmining.placebasedlpmdiscovery.model.discovery;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.FittingWindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryConfig;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.Exporter;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.MainFPGrowthLPMTree;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

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
            lpms = resTree.getLPMs(Integer.MAX_VALUE).stream().sorted(
                            Comparator.comparingDouble(lpm -> lpm.getAdditionalInfo().getEvaluationResult(
                                    StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                                    FittingWindowsEvaluationResult.class).getResult()))
                    .collect(Collectors.toList());
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
    public void keep(int lpmCount) {
        this.lpms = getAllLPMs().stream().sorted(
                Comparator.comparingDouble(lpm -> lpm.getAdditionalInfo().getEvaluationResult(
                        StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                        FittingWindowsEvaluationResult.class).getResult()))
                .collect(Collectors.toList())
                .subList(0, Math.min(getAllLPMs().size(), lpmCount));
    }

    @Override
    public void export(Exporter<LPMDiscoveryResult> exporter, OutputStream os) {
        exporter.export(this, os);
    }
}
