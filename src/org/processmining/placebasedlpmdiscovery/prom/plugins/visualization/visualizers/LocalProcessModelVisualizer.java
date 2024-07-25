package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.view.components.lpmdisplay.LPMDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.lpmdisplay.LPMPetriNetComponent;

import javax.swing.*;

public class LocalProcessModelVisualizer {

    @Plugin(name = "@0 Visualize Local Process Model", returnLabels = {"Visualized Local Process Model"},
            returnTypes = {JComponent.class}, parameterLabels = {"Local Process Model"}, userAccessible = false)
    @Visualizer
    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(PluginContext context, LocalProcessModel lpm) {
        LPMDisplayComponent lpmDisplayComponent = new LPMPetriNetComponent(lpm);
        return lpmDisplayComponent.getComponent();
    }


}
