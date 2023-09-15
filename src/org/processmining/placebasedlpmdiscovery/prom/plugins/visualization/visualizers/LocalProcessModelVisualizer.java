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
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentFactory;
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
        ViewSpecificAttributeMap map = new ViewSpecificAttributeMap();

        ReduceUsingMurataRulesPlugin reductorPlugin = new ReduceUsingMurataRulesPlugin();
        net = reductorPlugin.runDefault(context, net);

        for (org.processmining.models.graphbased.directed.petrinet.elements.Transition t : net.getNet().getTransitions()) {
            if (!t.isInvisible()) {
                String label = getLabel(t.getLabel(), lpm);
                map.putViewSpecific(t, AttributeMap.LABEL, label);

//                map.putViewSpecific(t, AttributeMap.EXTRALABELS, new String[]{""+lpm.getCount(t)});
//                map.putViewSpecific(t, AttributeMap.EXTRALABELPOSITIONS, new Point2D[]{new Point2D.Double(10, 10)});
            }
        }

        JComponent component = new JPanel();
        component.setLayout(new BoxLayout(component, BoxLayout.X_AXIS));
//        component.add((new CustomAcceptingPetriNetVisualizer()).visualize(context, net));
        component.add(ProMJGraphVisualizer.instance().visualizeGraph(context, net.getNet(), map));

        JComponent evalComponent = new JPanel();
        evalComponent.setLayout(new BoxLayout(evalComponent, BoxLayout.Y_AXIS));
        evalComponent.add(ComponentFactory.getComplexEvaluationResultComponent(
                lpm.getAdditionalInfo().getEvalResults().values()));
        evalComponent.add(new JLabel("Histogram"));
        component.add(evalComponent);
        return component;
    }

    private String getLabel(String trLabel, LocalProcessModel lpm) {
        EventCoverageEvaluationResult res = lpm.getAdditionalInfo()
                .getEvaluationResult(StandardLPMEvaluationResultId.EventCoverageEvaluationResult.name(),
                        EventCoverageEvaluationResult.class);

        return res == null ? trLabel : trLabel + ": " + System.lineSeparator() +
                res.getCoveredEventsCount(trLabel) + "/" + res.getEventCountPerActivity().get(trLabel);
    }
}
