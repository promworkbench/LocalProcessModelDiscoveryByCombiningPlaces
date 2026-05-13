package org.processmining.placebasedlpmdiscovery.view.components.diagnostics;

import org.processmining.lpms.diagnostics.LPMComplexityHistogram;
import org.processmining.placebasedlpmdiscovery.view.components.Component;
import org.processmining.placebasedlpmdiscovery.view.components.general.HistogramDrawingPanel;
import org.processmining.placebasedlpmdiscovery.view.components.general.HistogramEntries;

import javax.swing.*;
import java.awt.*;

public class LPMComplexityHistogramPanel extends JScrollPane implements Component {

    private static final Color PLACE_COLOR = new Color(70, 130, 180);
    private static final Color TRANSITION_COLOR = new Color(100, 180, 100);

    public LPMComplexityHistogramPanel(LPMComplexityHistogram histogram) {
        HistogramDrawingPanel placeChart = new HistogramDrawingPanel(
                HistogramEntries.from(histogram.getPlaceCountDistribution()),
                "Places per LPM", false);
        placeChart.setBarColor(PLACE_COLOR);

        HistogramDrawingPanel transitionChart = new HistogramDrawingPanel(
                HistogramEntries.from(histogram.getTransitionCountDistribution()),
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

    @Override
    public JComponent getComponent() {
        return this;
    }
}
