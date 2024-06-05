package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.dependencyinjection.LPMDiscoveryResultGuiceModule;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.DefaultLPMDiscoveryResultComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.dependencyinjection.PromViewGuiceModule;
import org.processmining.placebasedlpmdiscovery.view.controllers.DefaultLPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.models.DefaultLPMDiscoveryResultViewModel;

import javax.swing.*;

@Plugin(name = "@0 Visualize LPM Discovery Result",
        returnLabels = {"Visualized LPM Discovery Result"},
        returnTypes = {JComponent.class},
        parameterLabels = {"LPM Discovery Result"})
@Visualizer
public class LPMDiscoveryResultVisualizer {

    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, LPMDiscoveryResult result) {

        Injector injector = Guice.createInjector(new PromViewGuiceModule(), new LPMDiscoveryResultGuiceModule(result));

//        DefaultLPMDiscoveryResultViewModel model = new DefaultLPMDiscoveryResultViewModel(result);
//        DefaultLPMDiscoveryResultComponent view = new DefaultLPMDiscoveryResultComponent()

        DefaultLPMDiscoveryResultViewController controller =
                injector.getInstance(DefaultLPMDiscoveryResultViewController.class);

        controller.getView().display(controller.getModel());

        return controller.getView();
    }
}
