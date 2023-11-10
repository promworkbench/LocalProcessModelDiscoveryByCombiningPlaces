package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.main.MultipleLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.DefaultLPMDiscoveryResultComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.MultipleLPMDiscoveryResultComponent;
import org.processmining.placebasedlpmdiscovery.view.controllers.DefaultLPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.controllers.DefaultMultipleLPMDiscoveryResultsViewController;
import org.processmining.placebasedlpmdiscovery.view.controllers.LPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.controllers.MultipleLPMDiscoveryResultsViewController;
import org.processmining.placebasedlpmdiscovery.view.models.DefaultLPMDiscoveryResultViewModel;
import org.processmining.placebasedlpmdiscovery.view.models.DefaultMultipleLPMDiscoveryResultsViewModel;
import org.processmining.placebasedlpmdiscovery.view.models.MultipleLPMDiscoveryResultsViewModel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

@Plugin(name = "@0 Visualize LPM Discovery Result",
        returnLabels = {"Visualized LPM Discovery Result"},
        returnTypes = {JComponent.class},
        parameterLabels = {"LPM Discovery Result"})
@Visualizer
public class LPMDiscoveryResultVisualizer {

    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, LPMDiscoveryResult result) {

        DefaultLPMDiscoveryResultComponent view = new DefaultLPMDiscoveryResultComponent();
        DefaultLPMDiscoveryResultViewModel model = new DefaultLPMDiscoveryResultViewModel(result);

        LPMDiscoveryResultViewController controller =
                new DefaultLPMDiscoveryResultViewController(view, model);

        view.setListener(controller);
        view.display(model);

        return view;
    }
}
