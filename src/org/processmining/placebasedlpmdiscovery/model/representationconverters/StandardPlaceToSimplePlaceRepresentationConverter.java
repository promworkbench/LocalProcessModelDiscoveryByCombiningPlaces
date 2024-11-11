package org.processmining.placebasedlpmdiscovery.model.representationconverters;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.SimplePlace;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.util.stream.Collectors;

public class StandardPlaceToSimplePlaceRepresentationConverter implements PlaceRepresentationConverter<Place,
        SimplePlace<String>> {
    @Override
    public SimplePlace<String> convert(Place object) {
        return new SimplePlace<>(
                object.getInputTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet()),
                object.getOutputTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet())
        );
    }
}
