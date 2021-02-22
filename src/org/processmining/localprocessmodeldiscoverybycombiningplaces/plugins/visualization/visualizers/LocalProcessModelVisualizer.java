package org.processmining.localprocessmodeldiscoverybycombiningplaces.plugins.visualization.visualizers;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.model.LocalProcessModel;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.plugins.visualization.components.ComponentFactory;
import org.processmining.localprocessmodeldiscoverybycombiningplaces.utils.LocalProcessModelUtils;

import javax.swing.*;

public class LocalProcessModelVisualizer {

    @Plugin(name = "@0 Visualize Local Process Model", returnLabels = {"Visualized Local Process Model"},
            returnTypes = {JComponent.class}, parameterLabels = {"Local Process Model"}, userAccessible = false)
    @Visualizer
    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, LocalProcessModel lpm) {
        if (lpm == null)
            throw new IllegalArgumentException("The local process model to be visualized should not be null: " + lpm);
        AcceptingPetriNet net = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);

        JComponent component = new JPanel();
        component.setLayout(new BoxLayout(component, BoxLayout.X_AXIS));
        component.add((new CustomAcceptingPetriNetVisualizer()).visualize(context, net));
        component.add(ComponentFactory.getComplexEvaluationResultComponent(lpm.getAdditionalInfo().getEvaluationResult()));
        return component;
    }
}
