package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.stream.Collectors;

public class LEFRMatrixVisualizer {

    private static final int GAP = 1;
    private static final Font LABEL_FONT = new Font(Font.DIALOG, Font.PLAIN, 16);

    @Plugin(name = "@0 Visualize LEFR Matrix", returnLabels = {"Visualized LEFR Matrix"},
            returnTypes = {JComponent.class}, parameterLabels = {"LEFR Matrix"})
    @Visualizer
    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, LEFRMatrix lefr) {
        JPanel matrixPanel = new JPanel(new GridLayout(lefr.getRowSize() + 1, lefr.getColumnSize() + 1, GAP, GAP));
        matrixPanel.setBorder(BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP));
        matrixPanel.setBackground(Color.BLACK);

        Map<Integer, String> inverseMatrix = lefr.getNameIndexMap().entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        for (int row = 0; row < lefr.getRowSize() + 1; row++) {
            for (int col = 0; col < lefr.getColumnSize() + 1; col++) {
                String text = "";
                if (row == 0 && col == 0) ;
                else if (row == 0)
                    text = inverseMatrix.get(col - 1);
                else if (col == 0)
                    text = inverseMatrix.get(row - 1);
                else
                    text = String.valueOf(lefr.get(row - 1, col - 1));

                JLabel label = new JLabel(text, SwingConstants.CENTER);
                label.setFont(LABEL_FONT); // make it big
                label.setOpaque(true);
                label.setBackground(Color.WHITE);
                matrixPanel.add(label);
            }
        }

        JComponent component = new JScrollPane(matrixPanel);
        return component;
    }
}
