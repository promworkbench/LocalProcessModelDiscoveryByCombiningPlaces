package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Comparator;
import java.util.List;

public class RankedPlaceComparator implements Comparator<Place> {

    private final List<Pair<PlaceRankConverter, SortOrder>> converters;

    public RankedPlaceComparator(List<Pair<PlaceRankConverter, SortOrder>> converters) {
        this.converters = converters;
    }

    @Override
    public int compare(Place p1, Place p2) {
        for (Pair<PlaceRankConverter, SortOrder> entry : converters) {
            int cmp = Double.compare(entry.getKey().convert(p1), entry.getKey().convert(p2));
            if (cmp != 0) return entry.getValue() == SortOrder.ASCENDING ? cmp : -cmp;
        }
        return 0;
    }
}
