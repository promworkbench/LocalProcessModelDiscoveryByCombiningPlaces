package org.processmining.placebasedlpmdiscovery.view.components;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;
import java.util.Map;

public class DefaultComponentFactory implements ComponentFactory {

    private final LPMSetDisplayComponentFactory lpmSetDisplayComponentFactory;
    private final PlaceSetDisplayComponentFactory placeSetDisplayComponentFactory;
    private final ConfigurationComponentFactory configurationComponentFactory;

    @Inject
    public DefaultComponentFactory(LPMSetDisplayComponentFactory lpmSetDisplayComponentFactory,
                                   PlaceSetDisplayComponentFactory placeSetDisplayComponentFactory,
                                   ConfigurationComponentFactory configurationComponentFactory) {
        this.lpmSetDisplayComponentFactory = lpmSetDisplayComponentFactory;
        this.placeSetDisplayComponentFactory = placeSetDisplayComponentFactory;
        this.configurationComponentFactory = configurationComponentFactory;
    }

    @Override
    public LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                               Collection<LocalProcessModel> lpms,
                                                               Map<String, Object> parameters) {
        return this.lpmSetDisplayComponentFactory.createLPMSetDisplayComponent(type, lpms, parameters);
    }

    @Override
    public LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                               Collection<LocalProcessModel> lpms,
                                                               NewElementSelectedListener<LocalProcessModel> listener,
                                                               Map<String, Object> parameters) {
        return this.lpmSetDisplayComponentFactory.createLPMSetDisplayComponent(type, lpms, listener, parameters);
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

    @Override
    public ConfigurationComponentFactory getConfigurationComponentFactory() {
        return this.configurationComponentFactory;
    }
}
