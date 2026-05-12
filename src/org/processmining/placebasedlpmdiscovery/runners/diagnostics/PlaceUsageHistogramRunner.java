package org.processmining.placebasedlpmdiscovery.runners.diagnostics;

import org.processmining.lpms.diagnostics.PlaceUsageHistogram;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.runners.GUI.PBLPMDFrame;
import org.processmining.placebasedlpmdiscovery.view.components.diagnostics.PlaceUsageHistogramPanel;

import javax.swing.*;
import java.io.IOException;

public class PlaceUsageHistogramRunner extends PBLPMDFrame {

    public PlaceUsageHistogramRunner() throws Exception {
    }

    public static void main(String[] args) throws Exception {
        PBLPMDFrame frame = new PlaceUsageHistogramRunner();
        frame.setVisible(true);
    }

    @Override
    protected JComponent getComponent() throws IOException {
        FromFileLPMDiscoveryResult result = new FromFileLPMDiscoveryResult("data/diagnostics/lpm_set.json");
        return new PlaceUsageHistogramPanel(new PlaceUsageHistogram(result.getAllLPMs()));
    }
}
