package org.processmining.lpms.discovery.builders;

import org.processmining.placebasedlpmdiscovery.lpmbuilding.storage.WindowLPMStorage;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.SlidingWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.prom.PlacesProvider;

import java.util.Set;

public class PBLADAWindowLPMBuilder implements LADAWindowLPMBuilder {

    private final Set<Place> places;

    public PBLADAWindowLPMBuilder(PlacesProvider placesProvider) {
        this.places = placesProvider.provide();
    }

    @Override
    public WindowLPMStorage build(SlidingWindowInfo windowInfo, WindowLPMStorage prevWindowResult) {
//        Activity newAct = windowInfo.getWindow().get(windowInfo.getWindow().size()-1);
//        prevWindowResult.extend(newAct);
        return null;
    }
}
