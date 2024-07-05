package org.processmining.placebasedlpmdiscovery.view.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentId;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponentType;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.LPMResultPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.PlaceSetPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.DefaultComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponent;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponentType;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.DefaultConfigurationComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.StandardConfigurationComponentType;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.DefaultLPMSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.DefaultPlaceSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.GroupingSetupPanel;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity.*;

public class ViewGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PlaceSetDisplayComponentFactory.class).to(DefaultPlaceSetDisplayComponentFactory.class);
        bind(LPMSetDisplayComponentFactory.class).to(DefaultLPMSetDisplayComponentFactory.class);
        bind(ConfigurationComponentFactory.class).to(DefaultConfigurationComponentFactory.class);
        bind(ComponentFactory.class).to(DefaultComponentFactory.class);
        bind(new TypeLiteral<PluginVisualizerTableFactory<LocalProcessModel>>(){})
                .to(LPMResultPluginVisualizerTableFactory.class);
        bind(new TypeLiteral<PluginVisualizerTableFactory<Place>>(){})
                .to(PlaceSetPluginVisualizerTableFactory.class);

        MapBinder<String, LPMSimilaritySetupComponent> mapBinderLPMSimilaritySetupPanels = MapBinder.newMapBinder(binder(),
                String.class, LPMSimilaritySetupComponent.class);
        mapBinderLPMSimilaritySetupPanels.addBinding("Model Similarity")
                .to(ModelSimilarityLPMSimilaritySetupPanel.class);
        mapBinderLPMSimilaritySetupPanels.addBinding("Data Attributes")
                .to(DataAttributeLPMSimilaritySetupPanel.class);
        mapBinderLPMSimilaritySetupPanels.addBinding("Mixed")
                .to(MixedLPMSimilaritySetupPanel.class);

//        MapBinder<ConfigurationComponentType, ConfigurationComponent> mapBinderConfigurationComponents =
//                MapBinder.newMapBinder(binder(), ConfigurationComponentType.class, ConfigurationComponent.class);
//        mapBinderConfigurationComponents
//                .addBinding(StandardConfigurationComponentType.LPMSimilarityConfigurationComponent)
//                .to(LPMSimilarityChooserPanel.class);
//        mapBinderConfigurationComponents
//                .addBinding(StandardConfigurationComponentType.GroupingConfigurationComponent)
//                .to(GroupingSetupPanel.class);
    }
}
