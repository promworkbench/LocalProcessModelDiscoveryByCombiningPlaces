package org.processmining.placebasedlpmdiscovery.runners.diagnostics;

import nl.tue.astar.AStarException;
import org.processmining.lpms.diagnostics.EventCoverageHistogram;
import org.processmining.lpms.occurrence.LPMOccurrenceList;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.runners.GUI.PBLPMDFrame;
import org.processmining.placebasedlpmdiscovery.runners.lpmutils.LPMCoverageRunner;
import org.processmining.placebasedlpmdiscovery.view.components.diagnostics.EventCoverageHistogramPanel;

import javax.swing.*;
import java.util.Collection;
import java.util.stream.Collectors;

public class EventCoverageHistogramRunner extends PBLPMDFrame {

    public EventCoverageHistogramRunner() throws Exception {
    }

    public static void main(String[] args) throws Exception {
        PBLPMDFrame frame = new EventCoverageHistogramRunner();
        frame.setVisible(true);
    }

    @Override
    protected JComponent getComponent() throws Exception {
        FromFileLPMDiscoveryResult result = new FromFileLPMDiscoveryResult("data/diagnostics/lpm_set.json");
        EventLog log = XLogWrapper.fromFile("data/logs/bpi2012_res10939.xes");

        Collection<LPMOccurrenceList> occurrenceLists =
                result.getAllLPMs().stream()
                        .map(lpm -> {
                            try {
                                return LPMCoverageRunner.singleLPMtoLogAlignmentTax(lpm, log);
                            } catch (AStarException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.toSet());

        return new EventCoverageHistogramPanel(new EventCoverageHistogram(occurrenceLists, log.getEventCount()));
    }
}
