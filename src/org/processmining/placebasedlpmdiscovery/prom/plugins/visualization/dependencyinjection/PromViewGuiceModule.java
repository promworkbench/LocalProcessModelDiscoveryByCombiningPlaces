package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import org.processmining.placebasedlpmdiscovery.grouping.dependencyinjection.GroupingGuiceModule;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmdistances.dataattributes.DataAttributeModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.*;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.ComplexEvaluationResultPanel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.DefaultSettablePanelFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.GroupingSetupPanel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.SettablePanelFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity.DataAttributeLPMSimilaritySetupPanel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity.LPMSimilarityChooserPanel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity.MixedLPMSimilaritySetupPanel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity.ModelSimilarityLPMSimilaritySetupPanel;
import org.processmining.placebasedlpmdiscovery.view.controllers.DefaultLPMDiscoveryResultViewController;
import org.processmining.placebasedlpmdiscovery.view.controllers.LPMDiscoveryResultViewController;

import javax.swing.*;

public class PromViewGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new GroupingGuiceModule());

        MapBinder<ComponentId.Type, JPanel> mapBinderSettablePanels = MapBinder.newMapBinder(binder(),
                ComponentId.Type.class, JPanel.class);
        mapBinderSettablePanels.addBinding(ComponentId.Type.BasicLPMEvalMetrics).to(ComplexEvaluationResultPanel.class);
        mapBinderSettablePanels.addBinding(ComponentId.Type.Grouping).to(GroupingSetupPanel.class);

        MapBinder<String, JPanel> mapBinderLPMSimilaritySetupPanels = MapBinder.newMapBinder(binder(),
                String.class, JPanel.class);
        mapBinderLPMSimilaritySetupPanels.addBinding("Model Similarity")
                .to(ModelSimilarityLPMSimilaritySetupPanel.class);
        mapBinderLPMSimilaritySetupPanels.addBinding("Data Attributes")
                .to(DataAttributeLPMSimilaritySetupPanel.class);
        mapBinderLPMSimilaritySetupPanels.addBinding("Mixed")
                .to(MixedLPMSimilaritySetupPanel.class);

        MapBinder<LPMDViewComponentType, LPMDViewComponent> mapBinderLPMDViewComponents =
                MapBinder.newMapBinder(binder(), LPMDViewComponentType.class, LPMDViewComponent.class);
        mapBinderLPMDViewComponents.addBinding(LPMDViewComponentType.LPMSimilarityChooser).to(LPMSimilarityChooserPanel.class);


        bind(ComponentFactory.class).to(DefaultComponentFactory.class);
        bind(SettablePanelFactory.class).to(DefaultSettablePanelFactory.class);
        bind(LPMDiscoveryResultViewController.class).to(DefaultLPMDiscoveryResultViewController.class);
    }
}
