package org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.SimpleCollectionOfElementsComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;

public class DefaultPlaceSetDisplayComponentFactory implements PlaceSetDisplayComponentFactory {

    private final PluginVisualizerTableFactory<Place> tableFactory;

    @Inject
    public DefaultPlaceSetDisplayComponentFactory(PluginVisualizerTableFactory<Place> tableFactory) {
        this.tableFactory = tableFactory;
    }

    @Override
    public PlaceSetDisplayComponent createPlaceSetDisplayComponent(PlaceSetDisplayComponentType type,
                                                                   Collection<Place> places) {
        return createPlaceSetDisplayComponent(type, places, place -> {
        });
    }

    @Override
    public PlaceSetDisplayComponent createPlaceSetDisplayComponent(PlaceSetDisplayComponentType type,
                                                                   Collection<Place> places,
                                                                   NewElementSelectedListener<Place> listener) {
        if (type.equals(PlaceSetDisplayComponentType.SimplePlaceCollection)) {
            PlaceSetDisplayComponent component = new SimpleCollectionOfElementsComponent<>(null, places,
                    tableFactory, listener);
            return component;
        }
        throw new IllegalArgumentException("No implementation for type " + type.name());
    }
}
