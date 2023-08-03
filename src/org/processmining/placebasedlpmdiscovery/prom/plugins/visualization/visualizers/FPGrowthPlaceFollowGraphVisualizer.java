package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.FPGrowthPlaceFollowGraph;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.FPGrowthPlaceFollowGraphEdge;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.FPGrowthPlaceFollowGraphNode;
import org.processmining.models.graphbased.AttributeMap;
import org.processmining.models.graphbased.ViewSpecificAttributeMap;
import org.processmining.models.jgraph.ProMJGraphVisualizer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@Plugin(name = "@0 Visualize Place Follow Graph",
        returnLabels = {"Visualized Place Follow Graph"},
        returnTypes = {JComponent.class},
        parameterLabels = {"Place Follow Graph"})
@Visualizer
public class FPGrowthPlaceFollowGraphVisualizer implements ChangeListener {

    private UIPluginContext context;
    private FPGrowthPlaceFollowGraph graph;

    private JComponent container;
    private JSlider slider;

    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, FPGrowthPlaceFollowGraph graph) {
        if (graph.getEdges().size() < 1)
            return new JPanel();

        this.context = context;
        this.graph = graph;

        // container component
        this.container = new JPanel();
        this.container.setLayout(new BoxLayout(this.container, BoxLayout.Y_AXIS));

        // slider component
        JComponent sliderComponent = new JPanel();
        sliderComponent.setLayout(new BoxLayout(sliderComponent, BoxLayout.X_AXIS));

        JLabel label = new JLabel("Percentage of edges kept in the graph");
        sliderComponent.add(label);

        this.slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 10);
        this.slider.setName("Percentage of edges kept: ");
        this.slider.setMajorTickSpacing(10);
        this.slider.setPaintTicks(true);
        this.slider.setPaintLabels(true);
        this.slider.addChangeListener(this);
        sliderComponent.add(this.slider);

        this.container.add(sliderComponent);
        this.container.add(this.drawGraph(graph.discardEdges(100 - this.slider.getValue())));

        return this.container;
    }

    private JComponent drawGraph(FPGrowthPlaceFollowGraph graph) {
        // graph component
        ViewSpecificAttributeMap map = new ViewSpecificAttributeMap();
        for (FPGrowthPlaceFollowGraphNode node : graph.getNodes()) {
            map.putViewSpecific(node, AttributeMap.LABEL, node.getPlace().getShortString());
            map.putViewSpecific(node, AttributeMap.SHOWLABEL, true);
        }
        for (FPGrowthPlaceFollowGraphEdge edge : graph.getEdges()) {
            map.putViewSpecific(edge, AttributeMap.LABEL, Double.toString(edge.getWeight()));
            map.putViewSpecific(edge, AttributeMap.SHOWLABEL, true);
        }
        return ProMJGraphVisualizer.instance().visualizeGraph(context, graph, map);
    }

    @Override
    public void stateChanged(ChangeEvent changeEvent) {
        if (slider.getValueIsAdjusting())
            return;

        int percentage = slider.getValue();
        container.remove(1);
        container.add(this.drawGraph(this.graph.discardEdges(100 - percentage)));
        container.revalidate();
        container.repaint();
        container.revalidate();
    }
}
