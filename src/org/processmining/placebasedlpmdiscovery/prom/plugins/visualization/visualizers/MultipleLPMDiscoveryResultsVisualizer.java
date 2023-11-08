package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.main.MultipleLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.MultipleLPMDiscoveryResultComponent;
import org.processmining.placebasedlpmdiscovery.view.controllers.DefaultMultipleLPMDiscoveryResultsViewController;
import org.processmining.placebasedlpmdiscovery.view.controllers.MultipleLPMDiscoveryResultsViewController;
import org.processmining.placebasedlpmdiscovery.view.models.DefaultMultipleLPMDiscoveryResultsViewModel;
import org.processmining.placebasedlpmdiscovery.view.models.MultipleLPMDiscoveryResultsViewModel;
import org.processmining.placebasedlpmdiscovery.view.views.MultipleLPMDiscoveryResultsView;

import javax.swing.*;

@Plugin(name = "@0 Visualize Multiple LPM Results",
        returnLabels = {"Visualized Multiple LPM Result"},
        returnTypes = {JComponent.class},
        parameterLabels = {"Multiple LPM results"})
@Visualizer
public class MultipleLPMDiscoveryResultsVisualizer {

    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, MultipleLPMDiscoveryResults result) {

        MultipleLPMDiscoveryResultComponent view = new MultipleLPMDiscoveryResultComponent(context);
        MultipleLPMDiscoveryResultsViewModel model = new DefaultMultipleLPMDiscoveryResultsViewModel(result);

        MultipleLPMDiscoveryResultsViewController controller =
                new DefaultMultipleLPMDiscoveryResultsViewController(view, model);

        view.setListener(controller);
        view.display(model);

        return view;
    }
}
