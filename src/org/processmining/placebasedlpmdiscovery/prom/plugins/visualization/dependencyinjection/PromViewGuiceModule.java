package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import org.processmining.placebasedlpmdiscovery.grouping.dependencyinjection.GroupingGuiceModule;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentId;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.ComplexEvaluationResultPanel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.DefaultSettablePanelFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.GroupingSetupPanel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.SettablePanelFactory;
import org.processmining.placebasedlpmdiscovery.view.controllers.DefaultLPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.controllers.LPMDiscoveryResultViewController;

import javax.swing.*;

public class PromViewGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new GroupingGuiceModule());

        MapBinder<ComponentId.Type, JPanel> mapBinder = MapBinder.newMapBinder(binder(),
                ComponentId.Type.class, JPanel.class);
        mapBinder.addBinding(ComponentId.Type.BasicLPMEvalMetrics).to(ComplexEvaluationResultPanel.class);
        mapBinder.addBinding(ComponentId.Type.Grouping).to(GroupingSetupPanel.class);


        bind(SettablePanelFactory.class).to(DefaultSettablePanelFactory.class);
        bind(LPMDiscoveryResultViewController.class).to(DefaultLPMDiscoveryResultViewController.class);
    }
}
