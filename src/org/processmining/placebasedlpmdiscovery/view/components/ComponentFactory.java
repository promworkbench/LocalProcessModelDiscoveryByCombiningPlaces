package org.processmining.placebasedlpmdiscovery.view.components;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay.LPMSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponent;
import org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay.PlaceSetDisplayComponentType;
import org.processmining.placebasedlpmdiscovery.view.listeners.NewElementSelectedListener;

import java.util.Collection;

public interface ComponentFactory {

    LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                        Collection<LocalProcessModel> lpms);

    LPMSetDisplayComponent createLPMSetDisplayComponent(LPMSetDisplayComponentType type,
                                                        Collection<LocalProcessModel> lpms,
                                                        NewElementSelectedListener<LocalProcessModel> listener);

    PlaceSetDisplayComponent createPlaceSetDisplayComponent(PlaceSetDisplayComponentType type,
                                                            Collection<Place> places);

    PlaceSetDisplayComponent createPlaceSetDisplayComponent(PlaceSetDisplayComponentType type,
                                                            Collection<Place> places,
                                                            NewElementSelectedListener<Place> listener);
}
