package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class RankedPlace {

    private final Place place;
    private final List<Double> ranks;

    public RankedPlace(Place place, Double... ranks) {
        this.place = place;
        this.ranks = Arrays.stream(ranks).collect(Collectors.toList());
    }

    public List<Double> getRanks() {
        return ranks;
    }

    public Place getPlace() {
        return place;
    }
}
