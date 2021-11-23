package org.processmining.placebasedlpmdiscovery.model.fpgrowth;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TraceLPMFactory implements DataFactory<List<Place>, TraceLPMData> {

    private final List<Integer> trace;
    private final Map<String, Integer> labelMap;

    public TraceLPMFactory(List<Integer> trace, Map<String, Integer> labelMap) {
        this.trace = trace;
        this.labelMap = labelMap;
    }

    private boolean addPlaceToRlpmAndFire(Place place, ReplayableLocalProcessModel rlpm, int event) {
        // add transitions in the replayable lpm
        place.getInputTransitions().forEach(t -> rlpm.addTransition(labelMap.get(t.getLabel()), t.isInvisible()));
        place.getOutputTransitions().forEach(t -> rlpm.addTransition(labelMap.get(t.getLabel()), t.isInvisible()));

        // convert transitions in integers
        Set<Integer> inputTransitionIds = place.getInputTransitions()
                .stream()
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());
        Set<Integer> outputTransitionIds = place.getOutputTransitions()
                .stream()
                .map(t -> labelMap.get(t.getLabel()))
                .collect(Collectors.toSet());

        rlpm.addConstraint(0, outputTransitionIds, inputTransitionIds); // add the constraint
        return rlpm.fire(event); // fire the event
    }

    @Override
    public Optional<TraceLPMData> create(List<Place> places, int inputStartPosition, int inputEndPosition,
                                         TraceLPMData traceLPMData, int dataPosition) {
        Integer inputEvent = trace.get(inputStartPosition);
        if (places.size() < 1 || traceLPMData.getRlpm().getFiringSequence().contains(inputEvent))
            return Optional.empty();

        ReplayableLocalProcessModel newRlpm = new ReplayableLocalProcessModel(traceLPMData.getRlpm());

        if (!addPlaceToRlpmAndFire(places.get(0), newRlpm, inputEvent))
            return Optional.empty();
        Place previous = places.get(0);
        for (int i = 1; i < places.size(); ++i) {
            Place next = places.get(i);
            Transition silent = PlaceUtils.getCommonSilentTransition(previous, next);
            if (silent == null
                    || newRlpm.getFiringSequence().contains(labelMap.get(silent.getLabel()))
                    || !addPlaceToRlpmAndFire(next, newRlpm, labelMap.get(silent.getLabel())))
                return Optional.empty();
        }
        return Optional.of(new TraceLPMData(newRlpm, this.trace.get(inputEndPosition)));
    }

    @Override
    public TraceLPMData create() {
        return new TraceLPMData();
    }
}
