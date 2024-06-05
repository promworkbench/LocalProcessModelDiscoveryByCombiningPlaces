package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentId;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity.LPMSimilarityChooserPanel;

import javax.swing.*;
import java.util.Map;

public class DefaultSettablePanelFactory implements SettablePanelFactory {

    private final Map<ComponentId.Type, JPanel> mapBinder;

    @Inject
    public DefaultSettablePanelFactory(Map<ComponentId.Type, JPanel> mapBinder) {
        this.mapBinder = mapBinder;
    }

    @Override
    public JPanel getSettablePanel(ComponentId.Type type) {
        return mapBinder.get(type);
//        switch (type) {
//            case BasicLPMEvalMetrics:
//                return new ComplexEvaluationResultPanel();
//            case Grouping:
//                return new GroupingSetupPanel();
//        }
//        throw new IllegalArgumentException("The component type " + type + " is not supported.");
    }
}
