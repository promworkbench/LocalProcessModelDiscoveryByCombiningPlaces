package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.visualizers;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.placebasedlpmdiscovery.InputModule;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.prom.dependencyinjection.PromGuiceModule;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.SimpleCollectionOfElementsComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.LPMResultPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.dependencyinjection.PromViewGuiceModule;
import org.processmining.placebasedlpmdiscovery.service.dependencyinjection.ServiceGuiceModule;
import org.processmining.placebasedlpmdiscovery.view.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;

import javax.swing.*;


@Plugin(name = "@0 Visualize LPM Result",
        returnLabels = {"Visualized LPM Result"},
        returnTypes = {JComponent.class},
        parameterLabels = {"Petri Net Array"})
@Visualizer
public class LPMResultVisualizer {

    @PluginVariant(requiredParameterLabels = {0})
    public JComponent visualize(UIPluginContext context, LPMResult result) {
        if (result.size() < 1)
            return new JPanel();

        Injector guice = Guice.createInjector(new PromViewGuiceModule());
        ComponentFactory componentFactory = guice.getInstance(ComponentFactory.class);

        return componentFactory.createLPMSetDisplayComponent(LPMSetDisplayComponentType.SimpleLPMsCollection,
                result.getAllLPMs()).getComponent();
    }
}
