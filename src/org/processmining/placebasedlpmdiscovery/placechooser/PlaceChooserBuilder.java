package org.processmining.placebasedlpmdiscovery.placechooser;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.placechooser.placepredicates.PlacePredicate;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.PlaceRankConverter;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.RankedPlaceComparator;
import org.processmining.placebasedlpmdiscovery.placechooser.placerankconverters.SortOrder;
import org.processmining.placebasedlpmdiscovery.placechooser.placetransformers.PlaceTransformer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Fluent builder for {@link PlaceChooser}.
 *
 * <p>Assemble an ordered pipeline of {@link PlaceTransformer}s and {@link PlacePredicate}s,
 * plus one or more {@link PlaceRankConverter}s, then call {@link #build()} to obtain a
 * {@code PlaceChooser} that executes them in registration order.
 *
 * <p>Pipeline semantics:
 * <ul>
 *   <li><b>Transformers</b> mutate the place representation seen by subsequent steps.</li>
 *   <li><b>Filters</b> drop places that fail the predicate; dropped places are not ranked.</li>
 *   <li><b>Rank converter</b> scores each surviving place; lower scores sort first.</li>
 * </ul>
 *
 * <p>Example:
 * <pre>{@code
 * PlaceChooser chooser = PlaceChooser.builder()
 *         .withTransformer(new IncludedActivitiesPlaceTransformer(activities))
 *         .withFilter(new NonSelfLoopPlacePredicate())
 *         .rankBy(new TransitionCountPlaceRankConverter())   // primary key
 *         .rankBy(new TotalPassageCoveragePlaceRankConverter(lefr)) // tie-breaker
 *         .build();
 * }</pre>
 *
 * @see PlaceChooser#builder()
 * @see PlaceChooser#getDefault(EventLog)
 */
public class PlaceChooserBuilder {

    private enum StepType { TRANSFORMER, FILTER }

    private final List<PlaceTransformer> transformers = new ArrayList<>();
    private final List<PlacePredicate> filters = new ArrayList<>();
    private final List<StepType> order = new ArrayList<>();
    private final List<Pair<PlaceRankConverter, SortOrder>> rankConverters = new ArrayList<>();

    /**
     * Appends a transformer to the pipeline.
     *
     * <p>The transformer is applied to the current place representation before any subsequent
     * step sees it. Transformers run in the order they are registered.
     *
     * @param transformer the transformer to add
     * @return this builder
     */
    public PlaceChooserBuilder withTransformer(PlaceTransformer transformer) {
        transformers.add(transformer);
        order.add(StepType.TRANSFORMER);
        return this;
    }

    /**
     * Appends a filter to the pipeline.
     *
     * <p>Filters run in the order they are registered. If the predicate returns {@code false},
     * the place is immediately discarded and no later steps in the pipeline run for it.
     *
     * @param filter the predicate a place must satisfy to proceed
     * @return this builder
     */
    public PlaceChooserBuilder withFilter(PlacePredicate filter) {
        filters.add(filter);
        order.add(StepType.FILTER);
        return this;
    }

    /**
     * Appends a ranking criterion with ascending order (lower scores returned first).
     *
     * <p>Criteria are applied in registration order: the first is the primary sort key,
     * the second breaks ties, and so on. At least one criterion is required.
     *
     * @param rankConverter converts a place to a numeric score
     * @return this builder
     */
    public PlaceChooserBuilder rankBy(PlaceRankConverter rankConverter) {
        return rankBy(rankConverter, SortOrder.ASCENDING);
    }

    /**
     * Appends a ranking criterion with an explicit sort order.
     *
     * <p>Criteria are applied in registration order: the first is the primary sort key,
     * the second breaks ties, and so on. At least one criterion is required.
     *
     * @param rankConverter converts a place to a numeric score
     * @param order         {@link SortOrder#ASCENDING} returns lower scores first;
     *                      {@link SortOrder#DESCENDING} returns higher scores first
     * @return this builder
     */
    public PlaceChooserBuilder rankBy(PlaceRankConverter rankConverter, SortOrder order) {
        this.rankConverters.add(new Pair<>(rankConverter, order));
        return this;
    }

    /**
     * Builds the {@link PlaceChooser}.
     *
     * <p>The returned {@code PlaceChooser} runs each candidate place through the registered pipeline in
     * registration order: transformers rewrite the place, filters drop it if the predicate fails.
     * Surviving places are scored with the rank converter, sorted ascending, and the top {@code count}
     * are returned by {@link PlaceChooser#choose}.
     *
     * @return a new {@link PlaceChooser} that encapsulates the configured pipeline
     * @throws IllegalStateException if no rank converter has been set
     */
    public PlaceChooser build() {
        if (rankConverters.isEmpty()) {
            throw new IllegalStateException("At least one rank converter is required");
        }
        List<PlaceTransformer> capturedTransformers = new ArrayList<>(transformers);
        List<PlacePredicate> capturedFilters = new ArrayList<>(filters);
        List<StepType> capturedOrder = new ArrayList<>(order);
        List<Pair<PlaceRankConverter, SortOrder>> capturedConverters = new ArrayList<>(rankConverters);

        return (places, count) -> {
            List<Place> survivors = new ArrayList<>();
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
                    survivors.add(current);
                }
            }
            survivors.sort(new RankedPlaceComparator(capturedConverters));
            return survivors.stream()
                    .distinct()
                    .limit(count)
                    .collect(Collectors.toSet());
        };
    }
}
