package org.processmining.placebasedlpmdiscovery.plugins.visualization.visualizers;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinetclassicalreductor.plugins.ReduceUsingMurataRulesPlugin;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import javax.swing.*;

public class LocalProcessModelVisualizer {

    @Plugin(name = "@0 Visualize Local Process Model", returnLabels = {"Visualized Local Process Model"},
            returnTypes = {JComponent.class}, parameterLabels = {"Local Process Model"}, userAccessible = false)
    @Visualizer
    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(PluginContext context, LocalProcessModel lpm) {
        if (lpm == null)
            throw new IllegalArgumentException("The local process model to be visualized should not be null: " + lpm);
        AcceptingPetriNet net = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
        ReduceUsingMurataRulesPlugin reductorPlugin = new ReduceUsingMurataRulesPlugin();
        net = reductorPlugin.runDefault(context, net);

        JComponent component = new JPanel();
        component.setLayout(new BoxLayout(component, BoxLayout.X_AXIS));
        component.add((new CustomAcceptingPetriNetVisualizer()).visualize(context, net));

        JComponent evalComponent = new JPanel();
        evalComponent.setLayout(new BoxLayout(evalComponent, BoxLayout.Y_AXIS));
        evalComponent.add(ComponentFactory.getComplexEvaluationResultComponent(lpm.getAdditionalInfo().getEvaluationResult()));
        evalComponent.add(new JLabel("Histogram"));
        component.add(evalComponent);
        return component;
    }
}
