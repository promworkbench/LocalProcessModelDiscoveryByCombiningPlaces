package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.DefaultCategoryDataset;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.AttributeCollectorResult;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.DistinctValuesAttributeSummary;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.RangeAttributeSummary;
import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.TimestampAttributeSummary;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Read-only visualization of the event attribute context of the selected LPM: a bar chart of value counts for
 * literal/boolean attributes and a min-max range strip for numeric/timestamp attributes.
 */
public class LPMContextPanel extends JPanel {

    private static final int MAX_BARS = 10;
    private static final Color BAR_COLOR = new Color(70, 110, 170);
    private static final Color MEAN_COLOR = new Color(220, 120, 40);

    private final JPanel content;

    public LPMContextPanel() {
        this.setLayout(new BorderLayout());
        this.setBorder(new TitledBorder("LPM Context"));

        this.content = new JPanel();
        this.content.setLayout(new BoxLayout(this.content, BoxLayout.Y_AXIS));
        JScrollPane js = new JScrollPane(this.content);
        js.getVerticalScrollBar().setUnitIncrement(16);
        this.add(js, BorderLayout.CENTER);
    }

    private static double toDouble(Number number) {
        return number == null ? Double.NaN : number.doubleValue();
    }

    public void setModel(LPMCollectorResult result) {
        this.content.removeAll();
        if (!(result instanceof AttributeCollectorResult)) {
            this.content.add(new JLabel("No event-attribute context collected for this LPM."));
        } else {
            AttributeCollectorResult attrResult = (AttributeCollectorResult) result;
            List<String> keys = new ArrayList<>(attrResult.getAttributeKeys());
            keys.sort(String::compareTo);
            for (String key : keys) {
                attrResult.getAttributeSummaryForAttributeKey(key)
                        .ifPresent(summary -> addComponent(createAttributeComponent(key, summary)));
            }
        }
        this.content.revalidate();
        this.content.repaint();
    }

    private void addComponent(JComponent component) {
        component.setAlignmentX(Component.LEFT_ALIGNMENT);
        this.content.add(component);
        this.content.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private JComponent createAttributeComponent(String key, AttributeSummary<?, ?> summary) {
        Map<String, Number> features = summary.getRepresentationFeatures();
        if (summary instanceof DistinctValuesAttributeSummary) {
            return createBarChart(key, features);
        } else if (summary instanceof RangeAttributeSummary) {
            return createRangeStrip(key, features, summary instanceof TimestampAttributeSummary);
        }
        return new JLabel(key + ": unsupported attribute type " + summary.getClass().getSimpleName());
    }

    private JComponent createBarChart(String key, Map<String, Number> valueCounts) {
        List<Map.Entry<String, Number>> sorted = valueCounts.entrySet().stream()
                .filter(e -> e.getValue().doubleValue() > 0)
                .sorted((e1, e2) -> Double.compare(e2.getValue().doubleValue(), e1.getValue().doubleValue()))
                .collect(Collectors.toList());
        if (sorted.isEmpty()) {
            return new JLabel(key + ": no values");
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        long total = 0;
        long other = 0;
        for (int i = 0; i < sorted.size(); ++i) {
            long count = sorted.get(i).getValue().longValue();
            total += count;
            if (i < MAX_BARS) {
                dataset.addValue(count, "count", sorted.get(i).getKey());
            } else {
                other += count;
            }
        }
        if (other > 0) {
            dataset.addValue(other, "count", "other (" + (sorted.size() - MAX_BARS) + ")");
        }

        JFreeChart chart = ChartFactory.createBarChart(key + " (" + total + " events)", null, null,
                dataset, PlotOrientation.HORIZONTAL, false, true, false);
        chart.getTitle().setFont(chart.getTitle().getFont().deriveFont(12f));
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setShadowVisible(false);
        renderer.setSeriesPaint(0, BAR_COLOR);
        // leave room for the item labels at the end of the longest bar
        plot.getRangeAxis().setUpperMargin(0.15);
        renderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
        renderer.setBaseItemLabelsVisible(true);

        ChartPanel chartPanel = new ChartPanel(chart);
        // otherwise small charts are drawn at the minimum size and scaled down, which shrinks the fonts
        chartPanel.setMinimumDrawHeight(0);
        chartPanel.setMinimumDrawWidth(0);
        chartPanel.setMaximumDrawHeight(10000);
        chartPanel.setMaximumDrawWidth(10000);
        int height = 50 + 18 * dataset.getColumnCount();
        chartPanel.setPreferredSize(new Dimension(250, height));
        chartPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
        return chartPanel;
    }

    private JComponent createRangeStrip(String key, Map<String, Number> features, boolean isTimestamp) {
        Number min = features.get(AttributeSummary.MIN);
        Number max = features.get(AttributeSummary.MAX);
        // initial (empty) summaries have min > max, failed computations NaN
        if (min == null || max == null || Double.isNaN(min.doubleValue()) || Double.isNaN(max.doubleValue())
                || min.doubleValue() > max.doubleValue()) {
            return new JLabel(key + ": no values");
        }
        Number count = features.get(AttributeSummary.COUNT);
        String title = count != null ? key + " (" + count + " events)" : key;
        // the median is not shown since the incrementally computed MEDIAN feature equals the running mean
        return new RangeStripComponent(title, min.doubleValue(), max.doubleValue(),
                toDouble(features.get(AttributeSummary.MEAN)), isTimestamp);
    }

    /**
     * Draws a horizontal line from min to max with a marker for the mean.
     */
    private static class RangeStripComponent extends JComponent {

        private static final int HEIGHT = 72;
        private static final int PADDING = 10;

        private final String title;
        private final double min;
        private final double max;
        private final double mean;
        private final boolean isTimestamp;

        RangeStripComponent(String title, double min, double max, double mean, boolean isTimestamp) {
            this.title = title;
            this.min = min;
            this.max = max;
            this.mean = mean;
            this.isTimestamp = isTimestamp;

            this.setPreferredSize(new Dimension(250, HEIGHT));
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, HEIGHT));
            this.setToolTipText(String.format("min: %s, mean: %s, max: %s", format(min), format(mean), format(max)));
        }

        private String format(double value) {
            if (Double.isNaN(value)) {
                return "-";
            }
            if (this.isTimestamp) {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date((long) value));
            }
            return new DecimalFormat("#.##").format(value);
        }

        private int toX(double value, int left, int right) {
            if (this.max == this.min) {
                return (left + right) / 2;
            }
            return left + (int) Math.round((value - this.min) / (this.max - this.min) * (right - left));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());

            FontMetrics fm = g2.getFontMetrics();
            g2.setColor(Color.DARK_GRAY);
            g2.drawString(this.title, PADDING, fm.getAscent() + 2);

            String minLabel = format(this.min);
            String maxLabel = format(this.max);
            int left = PADDING;
            int right = getWidth() - PADDING;
            int lineY = 2 * fm.getHeight() + 12;

            // min-max line with end ticks
            g2.setStroke(new BasicStroke(2f));
            g2.setColor(BAR_COLOR);
            g2.drawLine(left, lineY, right, lineY);
            g2.drawLine(left, lineY - 5, left, lineY + 5);
            g2.drawLine(right, lineY - 5, right, lineY + 5);

            // min and max labels below the ends of the line
            g2.setColor(Color.DARK_GRAY);
            int labelY = lineY + 8 + fm.getAscent();
            g2.drawString(minLabel, left, labelY);
            g2.drawString(maxLabel, right - fm.stringWidth(maxLabel), labelY);

            // mean marker with its value above it
            if (!Double.isNaN(this.mean)) {
                int x = toX(this.mean, left, right);
                g2.setColor(MEAN_COLOR);
                g2.fillOval(x - 5, lineY - 5, 10, 10);
                String meanLabel = "mean " + format(this.mean);
                int labelX = Math.max(left, Math.min(x - fm.stringWidth(meanLabel) / 2,
                        right - fm.stringWidth(meanLabel)));
                g2.drawString(meanLabel, labelX, lineY - 8);
            }
            g2.dispose();
        }
    }
}
