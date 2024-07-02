package org.processmining.placebasedlpmdiscovery.view.dependencyinjection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.LPMResultPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.PlaceSetPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.DefaultComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.DefaultLPMSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.DefaultPlaceSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponentFactory;

public class ViewGuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(PlaceSetDisplayComponentFactory.class).to(DefaultPlaceSetDisplayComponentFactory.class);
        bind(LPMSetDisplayComponentFactory.class).to(DefaultLPMSetDisplayComponentFactory.class);
        bind(ComponentFactory.class).to(DefaultComponentFactory.class);
        bind(new TypeLiteral<PluginVisualizerTableFactory<LocalProcessModel>>(){})
                .to(LPMResultPluginVisualizerTableFactory.class);
        bind(new TypeLiteral<PluginVisualizerTableFactory<Place>>(){})
                .to(PlaceSetPluginVisualizerTableFactory.class);
    }
}
