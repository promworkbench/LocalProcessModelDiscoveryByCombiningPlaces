package org.processmining.placebasedlpmdiscovery.runners.diagnostics;

import org.processmining.lpms.diagnostics.TransitionUsageHistogram;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.runners.GUI.PBLPMDFrame;
import org.processmining.placebasedlpmdiscovery.view.components.diagnostics.TransitionUsageHistogramPanel;

import javax.swing.*;
import java.io.IOException;

public class TransitionUsageHistogramRunner extends PBLPMDFrame {

    public TransitionUsageHistogramRunner() throws IOException {
    }

    public static void main(String[] args) throws IOException {
        PBLPMDFrame frame = new TransitionUsageHistogramRunner();
        frame.setVisible(true);
    }

    @Override
    protected JComponent getComponent() throws IOException {
        FromFileLPMDiscoveryResult result = new FromFileLPMDiscoveryResult("data/diagnostics/lpm_set.json");
        return new TransitionUsageHistogramPanel(new TransitionUsageHistogram(result.getAllLPMs()));
    }
}
