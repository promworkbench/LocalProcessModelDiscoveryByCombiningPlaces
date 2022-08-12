package org.processmining.placebasedlpmdiscovery.plugins.visualization.components;

import javax.swing.*;

public class SettableComponentFactory {

    public JPanel getComponent(ComponentId.Type type) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        if (type.equals(ComponentId.Type.LogStatistics)) {
            panel.add(new JLabel("LOG STATISTICS"));
        } else if (type.equals(ComponentId.Type.BasicLPMEvalMetrics)) {
            panel.add(new JLabel("EVAL METRICS"));
        }
        return panel;
    }
}
