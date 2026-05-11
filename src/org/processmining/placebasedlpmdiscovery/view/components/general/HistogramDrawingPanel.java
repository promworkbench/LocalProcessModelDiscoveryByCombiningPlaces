package org.processmining.placebasedlpmdiscovery.view.components.general;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class HistogramDrawingPanel extends JPanel {

    static final int MARGIN = 50;
    static final int BAR_WIDTH = 40;
    static final int BAR_GAP = 10;
    static final Color DEFAULT_BAR_COLOR = new Color(70, 130, 180);
    private static final int CHART_HEIGHT = 200;
    private static final int TITLE_HEIGHT = 20;
    private static final int LABEL_AREA_HEIGHT_ROTATED = 80;
    private static final int LABEL_AREA_HEIGHT_PLAIN = 25;
    private static final int Y_TICKS = 5;
    private final List<Map.Entry<String, Integer>> entries;
    private final String title;
    private final boolean rotatedLabels;
    private Color barColor = DEFAULT_BAR_COLOR;

    public HistogramDrawingPanel(List<Map.Entry<String, Integer>> entries, String title,
                                 boolean rotatedLabels) {
        this.entries = entries;
        this.title = title;
        this.rotatedLabels = rotatedLabels;
        setBackground(Color.WHITE);
    }

    public void setBarColor(Color barColor) {
        this.barColor = barColor;
    }

    @Override
    public Dimension getPreferredSize() {
        int width = MARGIN * 2 + entries.size() * (BAR_WIDTH + BAR_GAP);
        int labelAreaHeight = rotatedLabels ? LABEL_AREA_HEIGHT_ROTATED : LABEL_AREA_HEIGHT_PLAIN;
        int height = MARGIN + TITLE_HEIGHT + CHART_HEIGHT + labelAreaHeight + MARGIN;
        return new Dimension(Math.max(width, 400), height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (entries.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int maxCount = entries.stream().mapToInt(Map.Entry::getValue).max().orElse(1);
        int chartY = MARGIN + TITLE_HEIGHT;
        int chartWidth = getPreferredSize().width - MARGIN;

        if (title != null && !title.isEmpty()) drawTitle(g2);

        drawAxes(g2, chartY, chartWidth);
        drawYAxisTicks(g2, chartY, chartWidth, maxCount);
        drawBars(g2, chartY, maxCount);
    }

    private void drawTitle(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.setColor(Color.BLACK);
        g2.drawString(title, MARGIN, MARGIN);
    }

    private void drawAxes(Graphics2D g2, int chartY, int chartWidth) {
        g2.setColor(Color.BLACK);
        g2.drawLine(MARGIN, chartY, MARGIN, chartY + CHART_HEIGHT);
        g2.drawLine(MARGIN, chartY + CHART_HEIGHT, chartWidth, chartY + CHART_HEIGHT);
    }

    private void drawYAxisTicks(Graphics2D g2, int chartY, int chartWidth, int maxCount) {
        g2.setFont(new Font("Arial", Font.PLAIN, 10));
        for (int i = 0; i <= Y_TICKS; i++) {
            int tickValue = (int) Math.round((double) maxCount * i / Y_TICKS);
            int y = chartY + CHART_HEIGHT - (int) ((double) tickValue / maxCount * CHART_HEIGHT);
            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(MARGIN + 1, y, chartWidth, y);
            g2.setColor(Color.BLACK);
            g2.drawLine(MARGIN - 3, y, MARGIN, y);
            String label = String.valueOf(tickValue);
            FontMetrics fm = g2.getFontMetrics();
            g2.drawString(label, MARGIN - 6 - fm.stringWidth(label), y + 4);
        }
    }

    private void drawBars(Graphics2D g2, int chartY, int maxCount) {
        int x = MARGIN + BAR_GAP;
        for (Map.Entry<String, Integer> entry : entries) {
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

            if (rotatedLabels) {
                drawRotatedLabel(g2, entry.getKey(), x + BAR_WIDTH / 2, chartY + CHART_HEIGHT + 5);
            } else {
                g2.drawString(entry.getKey(), x + (BAR_WIDTH - fm.stringWidth(entry.getKey())) / 2,
                        chartY + CHART_HEIGHT + 15);
            }

            x += BAR_WIDTH + BAR_GAP;
        }
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
