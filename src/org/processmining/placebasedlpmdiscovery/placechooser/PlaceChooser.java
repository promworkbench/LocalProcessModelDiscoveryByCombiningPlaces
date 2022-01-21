package org.processmining.placebasedlpmdiscovery.placechooser;

import com.google.common.collect.Sets;
import org.apache.commons.math3.util.Pair;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places.EmptyIOTransitionSetPlaceFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places.PlaceFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filterstrategies.places.SelfLoopPlaceFilter;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.fpgrowth.FPGrowthPlaceFollowGraphBuilder;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.FPGrowthPlaceFollowGraph;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.FPGrowthPlaceFollowGraphNode;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.*;
import java.util.stream.Collectors;

public class PlaceChooser {

    private final PlaceChooserParameters parameters;
    private Set<Place> places;
    private final XLog log;
    private final LEFRMatrix lefr;

    private List<Place> rankedPlaces;

    public PlaceChooser(PlaceChooserParameters parameters, Set<Place> places, XLog log, LEFRMatrix lefr) {
        this.parameters = parameters;
        this.places = new HashSet<>(places);
        this.log = log;
        this.lefr = lefr;
        postProcess();
        filter();
    }

    private void postProcess() {
        System.out.println("Place count before post-process: " + this.places.size());
        postProcessByIncludedActivities();
        postProcessByPassageUsage();
        System.out.println("Place count: " + this.places.size());
    }

    private void postProcessByPassageUsage() {
        System.out.println("==========Post-process Places - Passage Based==========");
        // get all pairs of activities that happen in the given window
        Set<Pair<String, String>> allFollowRelations = LogUtils
                .getFollowRelations(log, parameters.getFollowRelationsLimit());
        // for every place
        this.places.forEach(place -> {
            // Transitions to be removed
            Set<String> inTransitionsToBeRemoved = new HashSet<>();
            Set<String> outTransitionsToBeRemoved = new HashSet<>();

            // get all pairs of input-output transition labels
            Set<Pair<String, String>> placeInOutPair = PlaceUtils.getVisibleInoutPairsInPlace(place);
            // keep only the pairs that are not happening in the given window
            placeInOutPair.removeAll(allFollowRelations);
            // extract all labels that appear as input in the set of uncovered input-output transition pairs
            Set<String> inputs = placeInOutPair.stream().map(Pair::getKey).collect(Collectors.toSet());
            // map every input label with which output labels doesn't appear
            Map<String, Set<String>> forwardMapping = inputs
                    .stream()
                    .collect(Collectors.toMap(
                            x -> x,
                            x -> placeInOutPair
                                    .stream()
                                    .filter(p -> p.getKey().equals(x))
                                    .map(Pair::getValue)
                                    .collect(Collectors.toSet())));
            // mark all input transitions which don't happen with any of the output transitions
            for (String inTr : forwardMapping.keySet()) {
                if (place.getOutputTransitions().size() > 0 &&
                        place.getOutputTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet())
                                .equals(forwardMapping.get(inTr)))
                    inTransitionsToBeRemoved.add(inTr);
            }
            // extract all labels that appear as output in the set of uncovered input-output transition pairs
            Set<String> outputs = placeInOutPair.stream().map(Pair::getValue).collect(Collectors.toSet());
            // map every output label with which input labels doesn't appear
            Map<String, Set<String>> backwardMapping = outputs
                    .stream()
                    .collect(Collectors.toMap(
                            x -> x,
                            x -> placeInOutPair
                                    .stream()
                                    .filter(p -> p.getValue().equals(x))
                                    .map(Pair::getKey)
                                    .collect(Collectors.toSet())));
            // mark all output transitions which don't happen with any of the input transitions
            for (String outTr : backwardMapping.keySet()) {
                if (place.getInputTransitions().size() > 0 &&
                        place.getInputTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet())
                                .equals(backwardMapping.get(outTr)))
                    outTransitionsToBeRemoved.add(outTr);
            }

            // remove the transitions from the place
            for (String label : inTransitionsToBeRemoved)
                place.removeTransitions(label, true);
            for (String label : outTransitionsToBeRemoved)
                place.removeTransitions(label, false);

        });
        this.places = new HashSet<>(this.places);
        System.out.println("Place count: " + this.places.size());
    }

    private void postProcessByIncludedActivities() {
        System.out.println("==========Post-process Places - Included Activities==========");
        this.places.forEach(p -> PlaceUtils.filterTransitionsInPlace(p, parameters.getChosenActivities()));
        this.places = new HashSet<>(this.places);
        System.out.println("Place count: " + this.places.size());
    }

    private void filter() {
        filterWithFilters();
//        filterByIncludedActivities();
//        filterByCoveredPassages();
    }

    private void filterByCoveredPassages() {
        System.out.println("========Filtering places by covered passages========");
        System.out.println("Place count: " + places.size());
        Set<String> allFollowRelations = LogUtils.getFollowRelations(log, parameters.getFollowRelationsLimit())
                .stream()
                .map(pair -> pair.getKey() + " " + pair.getValue())
                .collect(Collectors.toSet());

        places.retainAll(places
                .stream()
                .filter(place -> {
                    Set<String> placeInOutCombinations = PlaceUtils.getVisibleInoutPairsInPlace(place)
                            .stream()
                            .map(pair -> pair.getKey() + " " + pair.getValue())
                            .collect(Collectors.toSet());
                    int coveredPassages = placeInOutCombinations
                            .stream()
                            .filter(allFollowRelations::contains)
                            .collect(Collectors.toSet())
                            .size();
                    return coveredPassages * 1.0 / placeInOutCombinations.size() >= parameters.getCoveredPassagesThreshold();
                })
                .collect(Collectors.toSet()));
        System.out.println("Place count: " + places.size());
    }

    private void filterByIncludedActivities() {
        System.out.println("========Filtering places by included activities========");
        System.out.println("Place count: " + places.size());
        Set<Place> placesForRemoval = places
                .stream()
                .filter(place -> {
                    for (Transition t : Sets.union(place.getInputTransitions(), place.getOutputTransitions())) {
                        if (!t.isInvisible()
                                && !parameters.getChosenActivities().contains(t.getLabel())
                                && !t.getLabel().toLowerCase().contains("start") // TODO: this should be made smartly
                                && !t.getLabel().toLowerCase().contains("end")) {
                            return true; // this place should be removed
                        }
                    }
                    return false; // this place should not be removed
                })
                .collect(Collectors.toSet());
        places.removeAll(placesForRemoval);
        System.out.println("Place count: " + places.size());
    }

    private void filterWithFilters() {
        // TODO: Replace this method with more complicated and flexible filtering structure
        PlaceFilter filter;

//        filter = new NonExistingPathsPlaceFilter();
//        places = filter.filter(places);

        // TODO: Or maybe it is fine we just add the place in the initial or final markings
        // Remove places that don't have any input or output transitions (for now)
        filter = new EmptyIOTransitionSetPlaceFilter();
        places.retainAll(filter.filter(places));
        System.out.println("After EmptyIO: " + places.size());

//        //Keep only base places. Places whose paths are fully covered by other places should be discarded.
//        filter = new PathCoveragePlaceFilter();
//        places = filter.filter(places);

        // Remove places that have the same input and output transitions
//        filter = new DuplicatePlaceFilter();
//        places.retainAll(filter.filter(places));
//        System.out.println("After Duplicate: " + places.size());

        filter = new SelfLoopPlaceFilter();
        places.retainAll(filter.filter(places));
        System.out.println("After SelfLoop: " + places.size());
    }

    private void rankPlaces() {
        System.out.println("========Ranking places========");
        Map<Place, Double> placeTransitionRankMap = new HashMap<>();
        int allPossibleTransitionsCount = parameters.getChosenActivities().size() * 2;
        places.forEach(place -> {
            int countTransitions = place.getInputTransitions().size() + place.getOutputTransitions().size();
            double transitionValue = countTransitions * 1.0 / allPossibleTransitionsCount;
            placeTransitionRankMap.put(place, transitionValue);
        });

        Map<Place, Double> placePassageRankMap = new HashMap<>();
        places.forEach(place -> {
            int passageCountSum = 0;
            int passageCounter = 0;
            for (Transition inTr : place.getInputTransitions()) {
                if (inTr.isInvisible())
                    continue;
                for (Transition outTr : place.getOutputTransitions()) {
                    if (outTr.isInvisible())
                        continue;
                    passageCountSum += lefr.get(inTr.getLabel(), outTr.getLabel());
                    passageCounter++;
                }
            }
            placePassageRankMap.put(place, passageCountSum * 1.0 / passageCounter);
        });

        rankedPlaces = new ArrayList<>(places);
        rankedPlaces.sort(Comparator
//                .<Place>comparingDouble(placePassageRankMap::get)
                .<Place>comparingDouble(placeTransitionRankMap::get)
//                .thenComparing(placePassageRankMap::get)
//                .thenComparing(placeTransitionRankMap::get)
                .thenComparing(Place::getShortString));
        Collections.reverse(rankedPlaces);
        System.out.println("========Ranking places ended========");
    }

    public Set<Place> choose() {
        return this.choose(parameters.getPlaceLimit());
    }

    public Set<Place> choose(int count) {
        if (rankedPlaces == null)
            rankPlaces();

        Set<Place> resSet = new HashSet<>(rankedPlaces.subList(0, Math.min(count, rankedPlaces.size())));
        Main.getAnalyzer().getStatistics().getPlaceStatistics().initializePlaceStatistics(resSet);
        return resSet;
    }

    public Set<Place> choose(int count, int degree) {
        FPGrowthPlaceFollowGraphBuilder graphBuilder = new FPGrowthPlaceFollowGraphBuilder(
                log,
                new HashSet<>(places),
                parameters.getFollowRelationsLimit());
        FPGrowthPlaceFollowGraph graph = graphBuilder.buildGraph();
        return graph.getNodesForDegree(count, degree).stream().map(FPGrowthPlaceFollowGraphNode::getPlace).collect(Collectors.toSet());
    }
}
