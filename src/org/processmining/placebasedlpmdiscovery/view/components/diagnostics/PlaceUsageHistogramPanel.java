package org.processmining.placebasedlpmdiscovery.view.components.diagnostics;

import org.processmining.lpms.diagnostics.PlaceUsageHistogram;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.view.components.Component;
import org.processmining.placebasedlpmdiscovery.view.components.general.HistogramDrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaceUsageHistogramPanel extends JScrollPane implements Component {

    static final int MARGIN = 50;

    private static final Color BAR_COLOR = new Color(70, 130, 180);

    public PlaceUsageHistogramPanel(PlaceUsageHistogram histogram) {
        List<Map.Entry<Place, Integer>> sorted = new ArrayList<>(histogram.getHistogram().entrySet());
        sorted.sort(Map.Entry.<Place, Integer>comparingByValue().reversed());

        List<Map.Entry<String, Integer>> entries = new ArrayList<>();
        for (Map.Entry<Place, Integer> e : sorted)
            entries.add(new AbstractMap.SimpleEntry<>(e.getKey().getShortString(), e.getValue()));

        HistogramDrawingPanel drawingPanel = new HistogramDrawingPanel(entries, "LPMs per Place", true);
        drawingPanel.setBarColor(BAR_COLOR);
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
