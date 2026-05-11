package org.processmining.placebasedlpmdiscovery.view.components.diagnostics;

import org.processmining.lpms.diagnostics.PlaceUsageHistogram;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.view.components.Component;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlaceUsageHistogramPanel extends JScrollPane implements Component {

    private static final int MARGIN = 50;
    private static final int BAR_WIDTH = 40;
    private static final int BAR_GAP = 10;
    private static final int LABEL_AREA_HEIGHT = 80;
    private static final int Y_TICKS = 5;
    private static final Color BAR_COLOR = new Color(70, 130, 180);

    public PlaceUsageHistogramPanel(PlaceUsageHistogram histogram) {
        List<Map.Entry<Place, Integer>> sortedEntries = new ArrayList<>(histogram.getHistogram().entrySet());
        sortedEntries.sort(Map.Entry.<Place, Integer>comparingByValue().reversed());
        int maxCount = sortedEntries.isEmpty() ? 1 : sortedEntries.get(0).getValue();

        setViewportView(new DrawingPanel(sortedEntries, maxCount, histogram.getTotalLPMs()));
        setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_NEVER);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    private static class DrawingPanel extends JPanel {

        private final List<Map.Entry<Place, Integer>> sortedEntries;
        private final int maxCount;
        private final int totalLPMs;

        private DrawingPanel(List<Map.Entry<Place, Integer>> sortedEntries, int maxCount, int totalLPMs) {
            this.sortedEntries = sortedEntries;
            this.maxCount = maxCount;
            this.totalLPMs = totalLPMs;
            setBackground(Color.WHITE);
        }

        @Override
        public Dimension getPreferredSize() {
            int width = MARGIN * 2 + sortedEntries.size() * (BAR_WIDTH + BAR_GAP);
            return new Dimension(Math.max(width, 400), 400);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (sortedEntries.isEmpty()) return;

            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int chartHeight = getHeight() - 2 * MARGIN - LABEL_AREA_HEIGHT;

            g2.setColor(Color.BLACK);
            g2.drawLine(MARGIN, MARGIN, MARGIN, MARGIN + chartHeight);
            g2.drawLine(MARGIN, MARGIN + chartHeight, getPreferredSize().width - MARGIN, MARGIN + chartHeight);

            drawYAxisTicks(g2, chartHeight);
            drawBars(g2, chartHeight);
            drawTotalLPMs(g2);
        }

        private void drawYAxisTicks(Graphics2D g2, int chartHeight) {
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            for (int i = 0; i <= Y_TICKS; i++) {
                int tickValue = (int) Math.round((double) maxCount * i / Y_TICKS);
                int y = MARGIN + chartHeight - (int) ((double) tickValue / maxCount * chartHeight);
                g2.setColor(Color.LIGHT_GRAY);
                g2.drawLine(MARGIN + 1, y, getPreferredSize().width - MARGIN, y);
                g2.setColor(Color.BLACK);
                g2.drawLine(MARGIN - 3, y, MARGIN, y);
                String label = String.valueOf(tickValue);
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(label, MARGIN - 6 - fm.stringWidth(label), y + 4);
            }
        }

        private void drawBars(Graphics2D g2, int chartHeight) {
            int x = MARGIN + BAR_GAP;
            for (Map.Entry<Place, Integer> entry : sortedEntries) {
                int barHeight = (int) ((double) entry.getValue() / maxCount * chartHeight);
                int barY = MARGIN + chartHeight - barHeight;

                g2.setColor(BAR_COLOR);
                g2.fillRect(x, barY, BAR_WIDTH, barHeight);
                g2.setColor(BAR_COLOR.darker());
                g2.drawRect(x, barY, BAR_WIDTH, barHeight);

                g2.setFont(new Font("Arial", Font.PLAIN, 10));
                g2.setColor(Color.BLACK);
                String countStr = String.valueOf(entry.getValue());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(countStr, x + (BAR_WIDTH - fm.stringWidth(countStr)) / 2, barY - 3);

                drawRotatedLabel(g2, entry.getKey().getShortString(), x + BAR_WIDTH / 2, MARGIN + chartHeight + 5);

                x += BAR_WIDTH + BAR_GAP;
            }
        }

        private void drawTotalLPMs(Graphics2D g2) {
            String label = "Total LPMs: " + totalLPMs;
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            g2.setColor(Color.BLACK);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, getPreferredSize().width - MARGIN - fm.stringWidth(label), MARGIN - 5);
        }

        private void drawRotatedLabel(Graphics2D g2, String label, int x, int y) {
            Graphics2D rotated = (Graphics2D) g2.create();
            rotated.setFont(new Font("Arial", Font.PLAIN, 9));
            rotated.setColor(Color.BLACK);
            rotated.translate(x, y);
            rotated.rotate(Math.PI / 4);
            rotated.drawString(label, 0, 0);
            rotated.dispose();
        }
    }
}
