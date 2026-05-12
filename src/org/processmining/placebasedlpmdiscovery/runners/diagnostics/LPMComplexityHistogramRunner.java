package org.processmining.placebasedlpmdiscovery.runners.diagnostics;

import org.processmining.lpms.diagnostics.LPMComplexityHistogram;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.runners.GUI.PBLPMDFrame;
import org.processmining.placebasedlpmdiscovery.view.components.diagnostics.LPMComplexityHistogramPanel;

import javax.swing.*;
import java.io.IOException;

public class LPMComplexityHistogramRunner extends PBLPMDFrame {

    public LPMComplexityHistogramRunner() throws Exception {
    }

    public static void main(String[] args) throws Exception {
        PBLPMDFrame frame = new LPMComplexityHistogramRunner();
        frame.setVisible(true);
    }

    @Override
    protected JComponent getComponent() throws IOException {
        FromFileLPMDiscoveryResult result = new FromFileLPMDiscoveryResult("data/diagnostics/lpm_set.json");
        return new LPMComplexityHistogramPanel(new LPMComplexityHistogram(result.getAllLPMs()));
    }
}
