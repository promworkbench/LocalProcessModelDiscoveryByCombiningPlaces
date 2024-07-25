package org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.SimpleCollectionOfElementsComponent;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.factories.PluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;

public class DefaultPlaceSetDisplayComponentFactory implements PlaceSetDisplayComponentFactory {

    private final PluginVisualizerTableFactory<Place> tableFactory;
    private final DataCommunicationControllerVM dcVM;

    @Inject
    public DefaultPlaceSetDisplayComponentFactory(PluginVisualizerTableFactory<Place> tableFactory, DataCommunicationControllerVM dcVM) {
        this.tableFactory = tableFactory;
        this.dcVM = dcVM;
    }

    @Override
    public PlaceSetDisplayComponent createPlaceSetDisplayComponent(PlaceSetDisplayComponentType type,
                                                                   Collection<Place> places) {
        return createPlaceSetDisplayComponent(type, places, null);
    }

    @Override
    public PlaceSetDisplayComponent createPlaceSetDisplayComponent(PlaceSetDisplayComponentType type,
                                                                   Collection<Place> places,
                                                                   NewElementSelectedListener<Place> listener) {
        if (type.equals(PlaceSetDisplayComponentType.SimplePlaceCollection)) {
            PlaceSetDisplayComponent component = new SimpleCollectionOfElementsComponent<>(places,
                    tableFactory, listener, this.dcVM);
            return component;
        }
        throw new IllegalArgumentException("No implementation for type " + type.name());
    }
}
