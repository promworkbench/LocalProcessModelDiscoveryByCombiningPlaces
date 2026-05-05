package org.processmining.placebasedlpmdiscovery.placechooser;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.PlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.PlaceRankConverter;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.RankedPlace;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.RankedPlaceComparator;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.PlaceTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceChooserBuilder {

    private enum StepType { TRANSFORMER, FILTER }

    private final List<PlaceTransformer> transformers = new ArrayList<>();
    private final List<PlacePredicate> filters = new ArrayList<>();
    private final List<StepType> order = new ArrayList<>();
    private PlaceRankConverter rankConverter;

    public PlaceChooserBuilder withTransformer(PlaceTransformer transformer) {
        transformers.add(transformer);
        order.add(StepType.TRANSFORMER);
        return this;
    }

    public PlaceChooserBuilder withFilter(PlacePredicate filter) {
        filters.add(filter);
        order.add(StepType.FILTER);
        return this;
    }

    public PlaceChooserBuilder withRankConverter(PlaceRankConverter rankConverter) {
        this.rankConverter = rankConverter;
        return this;
    }

    public PlaceChooser build() {
        if (rankConverter == null) {
            throw new IllegalStateException("A rank converter is required");
        }
        List<PlaceTransformer> capturedTransformers = new ArrayList<>(transformers);
        List<PlacePredicate> capturedFilters = new ArrayList<>(filters);
        List<StepType> capturedOrder = new ArrayList<>(order);
        PlaceRankConverter capturedConverter = rankConverter;

        return (places, count) -> {
            List<RankedPlace> ranked = new ArrayList<>();
            for (Place place : places) {
                Place current = place;
                boolean kept = true;
                int ti = 0, fi = 0;
                for (StepType type : capturedOrder) {
                    if (type == StepType.TRANSFORMER) {
                        current = capturedTransformers.get(ti++).apply(current);
                    } else {
                        if (!capturedFilters.get(fi++).test(current)) {
                            kept = false;
                            break;
                        }
                    }
                }
                if (kept) {
                    ranked.add(new RankedPlace(current, capturedConverter.convert(current)));
                }
            }
            ranked.sort(new RankedPlaceComparator());
            return ranked.stream()
                    .limit(count)
                    .map(RankedPlace::getPlace)
                    .collect(Collectors.toSet());
        };
    }
}
