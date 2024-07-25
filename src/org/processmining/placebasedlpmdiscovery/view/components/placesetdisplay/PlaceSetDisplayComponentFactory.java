package org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;

public interface PlaceSetDisplayComponentFactory {

    PlaceSetDisplayComponent createPlaceSetDisplayComponent(PlaceSetDisplayComponentType type,
                                                            Collection<Place> places);

    PlaceSetDisplayComponent createPlaceSetDisplayComponent(PlaceSetDisplayComponentType type,
                                                            Collection<Place> places,
                                                            NewElementSelectedListener<Place> listener);
}
