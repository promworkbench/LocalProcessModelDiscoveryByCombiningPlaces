package org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.function.Function;

/**
 * Scores a {@link Place} with a numeric rank used to order candidates in a
 * {@link org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooser PlaceChooser} pipeline.
 *
 * <p>Lower scores are returned first. Implement {@link #convert} to express whichever place property
 * should drive prioritisation (e.g. transition count (see {@link TransitionCountPlaceRankConverter}), passage
 * coverage (see {@link TotalPassageCoveragePlaceRankConverter})).
 */
public interface PlaceRankConverter extends Function<Place, Double> {

    /**
     * Returns a numeric score for the given place. Lower scores rank higher.
     *
     * @param place the place to score
     * @return a finite {@code double} representing the place's rank
     */
    Double convert(Place place);

    @Override
    default Double apply(Place place) {
        return convert(place);
    }
}
