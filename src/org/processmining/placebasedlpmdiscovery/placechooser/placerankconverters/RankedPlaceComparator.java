package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Comparator;
import java.util.List;

public class RankedPlaceComparator implements Comparator<Place> {

    private final List<PlaceRankConverter> converters;

    public RankedPlaceComparator(List<PlaceRankConverter> converters) {
        this.converters = converters;
    }

    @Override
    public int compare(Place p1, Place p2) {
        for (PlaceRankConverter converter : converters) {
            int cmp = Double.compare(converter.convert(p1), converter.convert(p2));
            if (cmp != 0) return cmp;
        }
        return 0;
    }
}
