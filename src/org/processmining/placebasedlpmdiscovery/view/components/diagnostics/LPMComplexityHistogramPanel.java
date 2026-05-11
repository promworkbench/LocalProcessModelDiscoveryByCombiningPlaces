package org.processmining.placebasedlpmdiscovery.view.components.diagnostics;

import org.processmining.lpms.diagnostics.LPMComplexityHistogram;
import org.processmining.placebasedlpmdiscovery.view.components.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LPMComplexityHistogramPanel extends JScrollPane implements Component {

    private static final int MARGIN = 50;
    private static final int BAR_WIDTH = 40;
    private static final int BAR_GAP = 10;
    private static final int CHART_HEIGHT = 200;
    private static final int CHART_GAP = 60;
    private static final int Y_TICKS = 5;
    private static final Color PLACE_COLOR = new Color(70, 130, 180);
    private static final Color TRANSITION_COLOR = new Color(100, 180, 100);

    public LPMComplexityHistogramPanel(LPMComplexityHistogram histogram) {
        List<Map.Entry<Integer, Integer>> placeEntries = sortedEntries(histogram.getPlaceCountDistribution());
        List<Map.Entry<Integer, Integer>> transitionEntries = sortedEntries(histogram.getTransitionCountDistribution());

        setViewportView(new DrawingPanel(placeEntries, transitionEntries));
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    private static List<Map.Entry<Integer, Integer>> sortedEntries(Map<Integer, Integer> distribution) {
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(distribution.entrySet());
        entries.sort(Map.Entry.comparingByKey());
        return entries;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    private static class DrawingPanel extends JPanel {

        private final List<Map.Entry<Integer, Integer>> placeEntries;
        private final List<Map.Entry<Integer, Integer>> transitionEntries;

        private DrawingPanel(List<Map.Entry<Integer, Integer>> placeEntries,
                             List<Map.Entry<Integer, Integer>> transitionEntries) {
            this.placeEntries = placeEntries;
            this.transitionEntries = transitionEntries;
            setBackground(Color.WHITE);
        }

        @Override
        public Dimension getPreferredSize() {
            int maxBars = Math.max(placeEntries.size(), transitionEntries.size());
            int width = MARGIN * 2 + maxBars * (BAR_WIDTH + BAR_GAP);
            int height = MARGIN * 2 + CHART_HEIGHT * 2 + CHART_GAP;
            return new Dimension(Math.max(width, 400), height);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int topChartY = MARGIN;
            int bottomChartY = MARGIN + CHART_HEIGHT + CHART_GAP;

            drawChartTitle(g2, "Places per LPM", topChartY);
            drawChart(g2, placeEntries, PLACE_COLOR, topChartY + 15);

            drawChartTitle(g2, "Transitions per LPM", bottomChartY);
            drawChart(g2, transitionEntries, TRANSITION_COLOR, bottomChartY + 15);
        }

        private void drawChartTitle(Graphics2D g2, String title, int y) {
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.setColor(Color.BLACK);
            g2.drawString(title, MARGIN, y);
        }

        private void drawChart(Graphics2D g2, List<Map.Entry<Integer, Integer>> entries, Color barColor, int chartY) {
            if (entries.isEmpty()) return;

            int maxCount = entries.stream().mapToInt(Map.Entry::getValue).max().orElse(1);

            g2.setColor(Color.BLACK);
            g2.drawLine(MARGIN, chartY, MARGIN, chartY + CHART_HEIGHT);
            g2.drawLine(MARGIN, chartY + CHART_HEIGHT, getPreferredSize().width - MARGIN, chartY + CHART_HEIGHT);

            drawYAxisTicks(g2, chartY, maxCount);
            drawBars(g2, entries, barColor, chartY, maxCount);
        }

        private void drawYAxisTicks(Graphics2D g2, int chartY, int maxCount) {
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            for (int i = 0; i <= Y_TICKS; i++) {
                int tickValue = (int) Math.round((double) maxCount * i / Y_TICKS);
                int y = chartY + CHART_HEIGHT - (int) ((double) tickValue / maxCount * CHART_HEIGHT);
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawLine(MARGIN + 1, y, getPreferredSize().width - MARGIN, y);
                g2.setColor(Color.BLACK);
                g2.drawLine(MARGIN - 3, y, MARGIN, y);
                String label = String.valueOf(tickValue);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(label, MARGIN - 6 - fm.stringWidth(label), y + 4);
            }
        }

        private void drawBars(Graphics2D g2, List<Map.Entry<Integer, Integer>> entries,
                              Color barColor, int chartY, int maxCount) {
            int x = MARGIN + BAR_GAP;
            for (Map.Entry<Integer, Integer> entry : entries) {
                int barHeight = (int) ((double) entry.getValue() / maxCount * CHART_HEIGHT);
                int barY = chartY + CHART_HEIGHT - barHeight;

                g2.setColor(barColor);
                g2.fillRect(x, barY, BAR_WIDTH, barHeight);
                g2.setColor(barColor.darker());
                g2.drawRect(x, barY, BAR_WIDTH, barHeight);

                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.setColor(Color.BLACK);
                String countStr = String.valueOf(entry.getValue());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(countStr, x + (BAR_WIDTH - fm.stringWidth(countStr)) / 2, barY - 3);

                String xLabel = String.valueOf(entry.getKey());
                g2.drawString(xLabel, x + (BAR_WIDTH - fm.stringWidth(xLabel)) / 2, chartY + CHART_HEIGHT + 15);

                x += BAR_WIDTH + BAR_GAP;
            }
        }
    }
}
