package org.processmining.placebasedlpmdiscovery.view.components.diagnostics;

import org.processmining.lpms.diagnostics.EventCoverageHistogram;
import org.processmining.placebasedlpmdiscovery.view.components.Component;
import org.processmining.placebasedlpmdiscovery.view.components.general.HistogramDrawingPanel;
import org.processmining.placebasedlpmdiscovery.view.components.general.HistogramEntries;

import javax.swing.*;

public class EventCoverageHistogramPanel extends JScrollPane implements Component {
    static final int MARGIN = 50;

    public EventCoverageHistogramPanel(EventCoverageHistogram histogram) {
        HistogramDrawingPanel drawingPanel = new HistogramDrawingPanel(
                HistogramEntries.from(histogram.getDistribution()), "Events by LPM Coverage Count", false);

        JLabel totalLabel = new JLabel("Total Events: " + histogram.getTotalEvents());
        totalLabel.setBorder(BorderFactory.createEmptyBorder(2, MARGIN, 2, 0));

        setViewportView(drawingPanel);
        setColumnHeaderView(totalLabel);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}
