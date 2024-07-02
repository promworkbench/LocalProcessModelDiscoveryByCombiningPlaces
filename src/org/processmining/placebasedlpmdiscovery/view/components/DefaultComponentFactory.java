package org.processmining.placebasedlpmdiscovery.view.components;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;

public class DefaultComponentFactory implements ComponentFactory {

    private final LPMSetDisplayComponentFactory lpmSetDisplayComponentFactory;
    private final PlaceSetDisplayComponentFactory placeSetDisplayComponentFactory;

    @Inject
    public DefaultComponentFactory(LPMSetDisplayComponentFactory lpmSetDisplayComponentFactory,
                                   PlaceSetDisplayComponentFactory placeSetDisplayComponentFactory) {
        this.lpmSetDisplayComponentFactory = lpmSetDisplayComponentFactory;
        this.placeSetDisplayComponentFactory = placeSetDisplayComponentFactory;
    }

    @Override
    public LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                               Collection<LocalProcessModel> lpms) {
        return this.lpmSetDisplayComponentFactory.createLPMSetDisplayComponent(type, lpms);
    }

    @Override
    public LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                               Collection<LocalProcessModel> lpms,
                                                               NewElementSelectedListener<LocalProcessModel> listener) {
        return this.lpmSetDisplayComponentFactory.createLPMSetDisplayComponent(type, lpms, listener);
    }

    @Override
    public PlaceSetDisplayComponent createPlaceSetDisplayComponent(PlaceSetDisplayComponentType type,
                                                                   Collection<Place> places) {
        return this.placeSetDisplayComponentFactory.createPlaceSetDisplayComponent(type, places);
    }

    @Override
    public PlaceSetDisplayComponent createPlaceSetDisplayComponent(PlaceSetDisplayComponentType type,
                                                                   Collection<Place> places,
                                                                   NewElementSelectedListener<Place> listener) {
        return this.placeSetDisplayComponentFactory.createPlaceSetDisplayComponent(type, places, listener);
    }
}
