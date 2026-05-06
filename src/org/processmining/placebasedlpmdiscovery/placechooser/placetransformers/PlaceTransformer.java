package org.processmining.placebasedlpmdiscovery.placechooser.placetransformers;

import org.processmining.placebasedlpmdiscovery.model.Place;

import java.util.function.Function;

/**
 * Transforms a place into a (possibly different) place before filtering or ranking.
 *
 * <p>Transformers are applied in registration order by {@link
 * org.processmining.placebasedlpmdiscovery.placechooser.PlaceChooserBuilder PlaceChooserBuilder}.
 * Each transformer receives the output of the previous one, so they compose naturally.
 */
public interface PlaceTransformer extends Function<Place, Place> {

    /**
     * Adapts the given place, returning a (possibly modified) place for the next pipeline step.
     *
     * @param place the input place
     * @return the transformed place; may be the same object or a new one
     */
    Place adapt(Place place);

    @Override
    default Place apply(Place place) {
        return adapt(place);
    }
}
