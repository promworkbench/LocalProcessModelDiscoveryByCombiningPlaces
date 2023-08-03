package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.InteractiveLPMsDiscovery;

import javax.swing.*;

public class InteractiveLPMsDiscoveryVisualizer {

    @Plugin(name = "@0 Visualize Interactive LPMs Discovery",
            returnLabels = {"Interactive LPMs Discovery"},
            returnTypes = {JComponent.class},
            parameterLabels = {"Interactive LPMs Discovery"})
    @Visualizer
    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(PluginContext context, InteractiveLPMsDiscovery interactiveLPMsDiscovery) {
        return interactiveLPMsDiscovery.getComponentForContext();
    }
}
