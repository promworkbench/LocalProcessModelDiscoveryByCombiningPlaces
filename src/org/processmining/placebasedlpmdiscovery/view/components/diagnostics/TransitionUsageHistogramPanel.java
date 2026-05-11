package org.processmining.placebasedlpmdiscovery.view.components.diagnostics;

import org.processmining.lpms.diagnostics.TransitionUsageHistogram;
import org.processmining.placebasedlpmdiscovery.view.components.Component;
import org.processmining.placebasedlpmdiscovery.view.components.general.HistogramDrawingPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransitionUsageHistogramPanel extends JScrollPane implements Component {

    static final int MARGIN = 50;

    public TransitionUsageHistogramPanel(TransitionUsageHistogram histogram) {
        List<Map.Entry<String, Integer>> entries = new ArrayList<>(histogram.getHistogram().entrySet());
        entries.sort(Map.Entry.<String, Integer>comparingByValue().reversed());

        HistogramDrawingPanel drawingPanel = new HistogramDrawingPanel(entries, "LPMs per Transition", true);
        setViewportView(drawingPanel);

        JLabel totalLabel = new JLabel("Total LPMs: " + histogram.getTotalLPMs());
        totalLabel.setBorder(BorderFactory.createEmptyBorder(2, MARGIN, 2, 0));
        setColumnHeaderView(totalLabel);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}
