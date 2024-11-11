package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMCollectorResultId;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.SimplePlace;
import org.processmining.placebasedlpmdiscovery.model.representationconverters.PlaceRepresentationConverter;
import org.processmining.placebasedlpmdiscovery.model.representationconverters.StandardPlaceToSimplePlaceRepresentationConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlaceVisitsCollectorResult implements LPMCollectorResult {

    private final Map<SimplePlace<String>, Integer> placeVisits;

    public PlaceVisitsCollectorResult(LocalProcessModel lpm) {
        placeVisits = new HashMap<>();
        PlaceRepresentationConverter<Place, SimplePlace<String>> converter =
                new StandardPlaceToSimplePlaceRepresentationConverter();
        for (Place place : lpm.getPlaces()) {
            placeVisits.put(converter.convert(place), 0);
        }
    }

    @Override
    public LPMCollectorResultId getId() {
        return StandardLPMCollectorResultId.PlaceVisitsCollectorResult;
    }

    public void updateUsedPlaces(Set<SimplePlace<String>> usedPlaces, int visitCounts) {
        usedPlaces.forEach(p -> placeVisits.put(p, placeVisits.get(p) + visitCounts));
    }
}
