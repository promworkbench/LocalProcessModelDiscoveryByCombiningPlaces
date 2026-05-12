package org.processmining.placebasedlpmdiscovery.view.components.diagnostics;

import org.processmining.lpms.diagnostics.EventCoverageHistogram;
import org.processmining.placebasedlpmdiscovery.view.components.Component;
import org.processmining.placebasedlpmdiscovery.view.components.general.HistogramDrawingPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventCoverageHistogramPanel extends JScrollPane implements Component {
    static final int MARGIN = 50;

    public EventCoverageHistogramPanel(EventCoverageHistogram histogram) {
        List<Map.Entry<Integer, Integer>> sorted = new ArrayList<>(histogram.getDistribution().entrySet());
        sorted.sort(Map.Entry.comparingByKey());

        List<Map.Entry<String, Integer>> entries = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : sorted)
            entries.add(new java.util.AbstractMap.SimpleEntry<>(String.valueOf(e.getKey()), e.getValue()));

        HistogramDrawingPanel drawingPanel = new HistogramDrawingPanel(entries, "Events by LPM Coverage Count", false);

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
