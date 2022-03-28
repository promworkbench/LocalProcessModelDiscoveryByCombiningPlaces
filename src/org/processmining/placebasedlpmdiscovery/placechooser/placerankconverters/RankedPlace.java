package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Arrays;
import java.util.Iterator;

public class RankedPlace {

    private final Place place;
    private final Iterator<Double> ranks;

    public RankedPlace(Place place, Double... ranks) {
        this.place = place;
        this.ranks = Arrays.stream(ranks).iterator();
    }

    public Iterator<Double> getRanks() {
        return ranks;
    }

    public Place getPlace() {
        return place;
    }
}
