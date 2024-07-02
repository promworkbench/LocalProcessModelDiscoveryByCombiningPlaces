package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinetclassicalreductor.plugins.ReduceUsingMurataRulesPlugin;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.ViewSpecificAttributeMap;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.EventCoverageEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.view.components.LPMDisplayComponent;
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
