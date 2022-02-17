package org.processmining.placebasedlpmdiscovery.placechooser;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Set;

public interface PlaceChooser {

    Set<Place> choose();
}
