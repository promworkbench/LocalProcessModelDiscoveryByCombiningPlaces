package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.dependencyinjection.LPMDiscoveryResultGuiceModule;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.prom.dependencyinjection.PromGuiceModule;
import org.processmining.placebasedlpmdiscovery.view.controllers.DefaultLPMDiscoveryResultViewController;

import javax.swing.*;

@Plugin(name = "@0 Visualize LPM Discovery Result",
        returnLabels = {"Visualized LPM Discovery Result"},
        returnTypes = {JComponent.class},
        parameterLabels = {"LPM Discovery Result"})
@Visualizer
public class LPMDiscoveryResultVisualizer {

    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, LPMDiscoveryResult result) {

        if (result.getInput() != null && result.getInput().getLog() != null) {
            Injector injector = Guice.createInjector(new LPMDiscoveryResultGuiceModule(result),
                    new PromGuiceModule(context));

//        DefaultLPMDiscoveryResultViewModel model = new DefaultLPMDiscoveryResultViewModel(result);
//        DefaultLPMDiscoveryResultComponent view = new DefaultLPMDiscoveryResultComponent()

            DefaultLPMDiscoveryResultViewController controller =
                    injector.getInstance(DefaultLPMDiscoveryResultViewController.class);

            controller.getView().display(controller.getModel());

            return controller.getView();
        }

        // if no event log, complex operations are not possible
        LPMResult lpmResult = new LPMResult(result);
        LPMResultVisualizer visualizer = new LPMResultVisualizer();
        return visualizer.visualize(context, lpmResult);
    }
}
