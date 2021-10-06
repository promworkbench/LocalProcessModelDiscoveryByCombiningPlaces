package org.processmining.placebasedlpmdiscovery.plugins.visualization.visualizers;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.ViewSpecificAttributeMap;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.jgraph.ProMJGraphVisualizer;
import org.processmining.models.semantics.petrinet.Marking;

import javax.swing.*;
import java.awt.*;

public class CustomAcceptingPetriNetVisualizer {

    @Plugin(name = "@0 Visualize Accepting Petri Net",
            returnLabels = {"Visualized Accepting Petri Net"},
            returnTypes = {JComponent.class},
            parameterLabels = {"Accepting Petri Net"},
            userAccessible = false)
    @Visualizer
    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, AcceptingPetriNet net) {
        ViewSpecificAttributeMap map = new ViewSpecificAttributeMap();
        for (Place place : net.getInitialMarking().baseSet()) {
            map.putViewSpecific(place, AttributeMap.FILLCOLOR, new Color(127, 0, 0));
        }
        for (Marking marking : net.getFinalMarkings()) {
            for (Place place : marking.baseSet()) {
                if (net.getInitialMarking().baseSet().contains(place)) {
                    map.putViewSpecific(place, AttributeMap.FILLCOLOR, new Color(127, 0, 127));
                } else {
                    map.putViewSpecific(place, AttributeMap.FILLCOLOR, new Color(0, 0, 127));
                }
            }
        }
        for (Transition t : net.getNet().getTransitions()) {
            if (!t.isInvisible()) {
                // map.putViewSpecific(t, AttributeMap.LABEL, lpm.getTransitions().get(t.getLabel()).getLabel());
                map.putViewSpecific(t, AttributeMap.LABEL, t.getLabel());
            }
        }

        return ProMJGraphVisualizer.instance().visualizeGraph(context, net.getNet(), map);
    }
}
