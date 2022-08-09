package org.processmining.placebasedlpmdiscovery.model;

import org.processmining.models.graphbased.NodeID;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.LPMAdditionalInfo;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The LocalProcessModel class is used to represent the logic for local process models. It contains places,
 * transitions and arcs between them.
 */
public class LocalProcessModel implements Serializable, TextDescribable {
    private static final long serialVersionUID = -2415080755970445804L;

    private String id;
    private final Set<Place> places;
    private final Map<String, Transition> transitions; // label -> transition map
    private final Set<Arc> arcs;
    private LPMAdditionalInfo additionalInfo;

    public LocalProcessModel() {
        this.id = UUID.randomUUID().toString();
        this.places = new HashSet<>();
        this.transitions = new HashMap<>();
        this.arcs = new HashSet<>();
        // setup additional info
        this.additionalInfo = new LPMAdditionalInfo(this);
//        this.additionalInfo.setLogFrequency(0);
    }

    public LocalProcessModel(LocalProcessModel lpm) {
        this(); // TODO: this is not a deep copy, both lpms will contain the same places

        this.id = lpm.id;
        this.places.addAll(lpm.places);
        this.arcs.addAll(lpm.arcs);
        this.transitions.putAll(lpm.transitions);
        this.setAdditionalInfo(new LPMAdditionalInfo(lpm.additionalInfo));
    }

    public LocalProcessModel(Place place) {
        this();
        this.addPlace(place);
    }

    /**
     * Finds all transitions that don't have output arcs with any place in the LPM
     * @return transitions for which there is no place with output arc toward them
     */
    public List<Transition> getInputTransitions() {
        List<Transition> res = new ArrayList<>();
        for (Transition transition : transitions.values()) {
            boolean is_input = true;
            for (Place place : places) {
                if (place.isOutputTransition(transition))
                    is_input = false;
            }
            if (is_input)
                res.add(transition);
        }
        return res;
    }

    /**
     * Finds all transitions that don't have input arcs with any place in the LPM
     * @return transitions for which there is no place with input arc toward them
     */
    public List<Transition> getOutputTransitions() {
        List<Transition> res = new ArrayList<>();
        for (Transition transition : transitions.values()) {
            boolean is_output = true;
            for (Place place : places) {
                if (place.isInputTransition(transition))
                    is_output = false;
            }
            if (is_output)
                res.add(transition);
        }
        return res;
    }

    public LPMAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(LPMAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public Set<Arc> getArcs() {
        return arcs;
    }

    public String getId() {
        return id;
    }

    public Set<Place> getPlaces() {
        return places;
    }

    public void addPlace(Place place) {
        if (place == null)
            throw new IllegalArgumentException("The place to be added should not be null: " + place);
        if (this.containsPlace(place))
            return;

        places.add(place);

        for (Transition transition : place.getInputTransitions()) {
            Arc arc = new Arc(place, transition, true);
            arcs.add(arc);
            if (!transitions.containsKey(transition.getLabel()))
                transitions.put(transition.getLabel(), transition);
        }

        for (Transition transition : place.getOutputTransitions()) {
            Arc arc = new Arc(place, transition, false);
            arcs.add(arc);
            if (!transitions.containsKey(transition.getLabel()))
                transitions.put(transition.getLabel(), transition);
        }

        this.additionalInfo.clearEvaluation();
    }

    public void addAllPlaces(Set<Place> places) {
        for (Place place : places)
            this.addPlace(place);
    }

    public void addLPM(LocalProcessModel lpm) {
        for (Place place : lpm.getPlaces())
            this.addPlace(place);
    }

    public boolean containsPlace(Place place) {
        return this.places.contains(place);
    }

    public boolean containsPlace(Set<String> possibleShortString) {
        for (Place place : this.places)
            if (possibleShortString.contains(place.getShortString()))
                return true;
        return false;
    }

    public boolean containsLPM(LocalProcessModel lpm) {
        for (Place place : lpm.getPlaces())
            if (!this.containsPlace(place))
                return false;

        return true;
    }

    /**
     * Checks whether the LPM and the place have common transitions that can be used in order to add the place
     * in the LPM.
     * @param place: The place for which we check if there are common transitions with the LPM
     * @return true if there are common transitions and false otherwise
     */
    public boolean hasCommonTransitions(Place place) {
        Set<Transition> resSet = new HashSet<>();
        resSet.addAll(place.getInputTransitions());
        resSet.addAll(place.getOutputTransitions());
        resSet.retainAll(this.transitions.values());
        return !resSet.isEmpty();
    }

    public Collection<Transition> getTransitions() {
        return transitions.values();
    }

    public Collection<Transition> getVisibleTransitions() {
        return transitions.values()
                .stream()
                .filter(t -> !t.isInvisible())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("LPM: ").append(this.id).append("\n");
        for (Place place : this.places) {
            sb.append(place.getShortString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;

        LocalProcessModel lpm = (LocalProcessModel) obj;
        return this.places.equals(lpm.places);
    }

    @Override
    public int hashCode() {
        return Objects.hash(places);
    }

    @Override
    public String getShortString() {
        StringBuilder sb = new StringBuilder();
        for (Place place : places)
            sb.append(place.getShortString());
        return sb.toString();
    }

    public Place getPlace(NodeID id) {
        return null;
    }
}