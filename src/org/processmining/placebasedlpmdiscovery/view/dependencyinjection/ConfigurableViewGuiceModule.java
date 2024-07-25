package org.processmining.placebasedlpmdiscovery.view.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.MapBinder;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.DefaultConfigurationComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity.DataAttributeLPMSimilaritySetupPanel;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity.LPMSimilaritySetupComponent;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity.MixedLPMSimilaritySetupPanel;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity.ModelSimilarityLPMSimilaritySetupPanel;

public class ConfigurableViewGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ConfigurationComponentFactory.class).to(DefaultConfigurationComponentFactory.class);

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
