package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.main.MultipleLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.SimpleCollectionOfElementsComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.controlcomponents.TwoLPMDiscoveryResultsComparisonComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.LPMResultPluginVisualizerTableFactory;

import javax.swing.*;
import java.awt.*;

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

        JPanel base = new JPanel();
        base.setLayout(new BoxLayout(base, BoxLayout.X_AXIS));

        JComponent tablePanel = new SimpleCollectionOfElementsComponent<>(
                context,
                result.getAllLPMs(),
                new LPMResultPluginVisualizerTableFactory());
        JPanel settablePanels = new TwoLPMDiscoveryResultsComparisonComponent();

        // set the preferred dimension of the two containers
        int windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        tablePanel.setPreferredSize(new Dimension(80 * windowWidth / 100, windowHeight));
        settablePanels.setPreferredSize(new Dimension(15 * windowWidth / 100, windowHeight));

        // add the table and LPM visualization containers and add some space between them
        base.add(tablePanel);
        base.add(Box.createRigidArea(new Dimension(windowWidth / 100, windowHeight)));
        base.add(settablePanels);

        // take a union of the nets

        // for each lpm measures on both logs are shown

        // passage usage numbers can be shown on both logs

        return base;
    }
}
