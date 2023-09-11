package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.main.MultipleLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.SimpleCollectionOfElementsComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.LPMResultPluginVisualizerTableFactory;

import javax.swing.*;

@Plugin(name = "@0 Visualize Multiple LPM Results",
        returnLabels = {"Visualized Multiple LPM Result"},
        returnTypes = {JComponent.class},
        parameterLabels = {"Multiple LPM results"})
@Visualizer
public class MultipleLPMDiscoveryResultsVisualizer {

    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, MultipleLPMDiscoveryResults result) {

        if (result.getResults().size() < 1)
            return new JPanel();

        return new SimpleCollectionOfElementsComponent<>(
                context, (LPMResult) result.getResults().stream().findAny().get(), new LPMResultPluginVisualizerTableFactory());
    }
}
