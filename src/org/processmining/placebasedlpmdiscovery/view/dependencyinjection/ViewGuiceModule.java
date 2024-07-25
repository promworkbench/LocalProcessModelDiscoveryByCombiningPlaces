package org.processmining.placebasedlpmdiscovery.view.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMResultPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.DefaultComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.DefaultConfigurationComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.DefaultLPMSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.DefaultPlaceSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity.*;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.dependencyinjection.DataCommunicationGuiceModuleVM;

public class ViewGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        install(new DataCommunicationGuiceModuleVM());
        bind(PlaceSetDisplayComponentFactory.class).to(DefaultPlaceSetDisplayComponentFactory.class);
        bind(LPMSetDisplayComponentFactory.class).to(DefaultLPMSetDisplayComponentFactory.class);
        bind(ComponentFactory.class).to(DefaultComponentFactory.class);
        bind(new TypeLiteral<PluginVisualizerTableFactory<LocalProcessModel>>(){})
                .to(LPMResultPluginVisualizerTableFactory.class);
        bind(new TypeLiteral<PluginVisualizerTableFactory<Place>>(){})
                .to(PlaceSetPluginVisualizerTableFactory.class);
    }
}
