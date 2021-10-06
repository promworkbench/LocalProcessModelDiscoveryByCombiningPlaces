package org.processmining.placebasedlpmdiscovery.plugins.visualization.visualizers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.TableAndVisualizerForCollectionOfElementsComponent;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories.LPMResultPluginVisualizerTableFactory;

import javax.swing.*;


@Plugin(name = "@0 Visualize LPM Result",
        returnLabels = {"Visualized LPM Result"},
        returnTypes = {JComponent.class},
        parameterLabels = {"Petri Net Array"})
@Visualizer
public class LPMResultVisualizer {

    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, LPMResult result) {

        if (result.size() < 1)
            return new JPanel();

        return new TableAndVisualizerForCollectionOfElementsComponent<>(
                context, result, new LPMResultPluginVisualizerTableFactory());
    }
}
