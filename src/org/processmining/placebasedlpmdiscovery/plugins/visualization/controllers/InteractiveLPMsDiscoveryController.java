package org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.plugins.mining.InteractiveLPMsDiscovery;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.TableAndVisualizerForCollectionOfElementsComponent;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories.LPMResultPluginVisualizerTableFactory;

import javax.swing.*;

public class InteractiveLPMsDiscoveryController {

    private final JComponent content;

    public InteractiveLPMsDiscoveryController(UIPluginContext context, InteractiveLPMsDiscovery interactiveLPMsDiscovery) {
        LPMResult result = interactiveLPMsDiscovery.getResult();
        if (result.size() < 1)
            content = new JPanel();
        else
            content = new TableAndVisualizerForCollectionOfElementsComponent<>(
                    context, result, new LPMResultPluginVisualizerTableFactory());
    }

    public JComponent getComponent() {
        return content;
    }
}
