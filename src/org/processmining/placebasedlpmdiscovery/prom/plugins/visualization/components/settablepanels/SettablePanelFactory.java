package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentId;

import javax.swing.*;

public class SettablePanelFactory {

    public JPanel getSettablePanel(ComponentId.Type type) {
        switch (type) {
            case BasicLPMEvalMetrics:
                return new ComplexEvaluationResultPanel();
            case Grouping:
                return new GroupingSetupPanel();
        }
        throw new IllegalArgumentException("The component type " + type + " is not supported.");
    }
}
