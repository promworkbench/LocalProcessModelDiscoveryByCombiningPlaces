package org.processmining.lpms.diagnostics;

import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PlaceUsageHistogram {

    private final Map<Place, Integer> histogram;
    private final int totalLPMs;

    public PlaceUsageHistogram(Collection<LocalProcessModel> lpms) {
        this.histogram = new HashMap<>();
        this.totalLPMs = lpms.size();
        for (LocalProcessModel lpm : lpms) {
            for (Place place : lpm.getPlaces()) {
                histogram.merge(place, 1, Integer::sum);
            }
        }
    }

    public int getTotalLPMs() {
        return totalLPMs;
    }

    public Map<Place, Integer> getHistogram() {
        return histogram;
    }

    public int getCount(Place place) {
        return histogram.getOrDefault(place, 0);
    }
}
