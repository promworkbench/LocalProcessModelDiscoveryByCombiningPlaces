package org.processmining.placebasedlpmdiscovery.view.components.diagnostics;

import org.processmining.lpms.diagnostics.LPMComplexityHistogram;
import org.processmining.placebasedlpmdiscovery.view.components.Component;
import org.processmining.placebasedlpmdiscovery.view.components.general.HistogramDrawingPanel;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LPMComplexityHistogramPanel extends JScrollPane implements Component {

    private static final Color PLACE_COLOR = new Color(70, 130, 180);
    private static final Color TRANSITION_COLOR = new Color(100, 180, 100);

    public LPMComplexityHistogramPanel(LPMComplexityHistogram histogram) {
        HistogramDrawingPanel placeChart = new HistogramDrawingPanel(
                toStringEntries(histogram.getPlaceCountDistribution()),
                "Places per LPM", false);
        placeChart.setBarColor(PLACE_COLOR);

        HistogramDrawingPanel transitionChart = new HistogramDrawingPanel(
                toStringEntries(histogram.getTransitionCountDistribution()),
                "Transitions per LPM", false);
        transitionChart.setBarColor(TRANSITION_COLOR);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.add(placeChart);
        container.add(transitionChart);

        setViewportView(container);
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private static List<Map.Entry<String, Integer>> toStringEntries(Map<Integer, Integer> distribution) {
        List<Map.Entry<Integer, Integer>> sorted = new ArrayList<>(distribution.entrySet());
        sorted.sort(Map.Entry.comparingByKey());
        List<Map.Entry<String, Integer>> entries = new ArrayList<>();
        for (Map.Entry<Integer, Integer> e : sorted)
            entries.add(new AbstractMap.SimpleEntry<>(String.valueOf(e.getKey()), e.getValue()));
        return entries;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}
