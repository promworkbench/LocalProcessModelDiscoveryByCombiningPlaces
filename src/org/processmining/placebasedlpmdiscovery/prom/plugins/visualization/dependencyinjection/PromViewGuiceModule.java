package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import org.processmining.placebasedlpmdiscovery.grouping.dependencyinjection.GroupingGuiceModule;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.*;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.ComplexEvaluationResultPanel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.DefaultSettablePanelFactory;
import org.processmining.placebasedlpmdiscovery.service.dependencyinjection.ServiceGuiceModule;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.GroupingSetupPanel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.SettablePanelFactory;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity.*;
import org.processmining.placebasedlpmdiscovery.view.controllers.DefaultLPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.controllers.LPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.dependencyinjection.DataCommunicationGuiceModuleVM;
import org.processmining.placebasedlpmdiscovery.view.dependencyinjection.ConfigurableViewGuiceModule;
import org.processmining.placebasedlpmdiscovery.view.dependencyinjection.ViewGuiceModule;

import javax.swing.*;

public class PromViewGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new GroupingGuiceModule());
        install(new ServiceGuiceModule());
        install(new DataCommunicationGuiceModuleVM());
        install(new ConfigurableViewGuiceModule());
        install(new ViewGuiceModule());

        MapBinder<ComponentId.Type, JPanel> mapBinderSettablePanels = MapBinder.newMapBinder(binder(),
                ComponentId.Type.class, JPanel.class);
        mapBinderSettablePanels.addBinding(ComponentId.Type.BasicLPMEvalMetrics).to(ComplexEvaluationResultPanel.class);
        mapBinderSettablePanels.addBinding(ComponentId.Type.Grouping).to(GroupingSetupPanel.class);

        MapBinder<LPMDViewComponentType, LPMDViewComponent> mapBinderLPMDViewComponents =
                MapBinder.newMapBinder(binder(), LPMDViewComponentType.class, LPMDViewComponent.class);
//        mapBinderLPMDViewComponents.addBinding(LPMDViewComponentType.LPMSimilarityChooser).to(LPMSimilarityChooserPanel.class);


        bind(ComponentFactory.class).to(DefaultComponentFactory.class);
        bind(SettablePanelFactory.class).to(DefaultSettablePanelFactory.class);
        bind(LPMDiscoveryResultViewController.class).to(DefaultLPMDiscoveryResultViewController.class);
    }
}
