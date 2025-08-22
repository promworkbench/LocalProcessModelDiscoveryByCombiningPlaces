package org.processmining.placebasedlpmdiscovery.model;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.PlaceAdditionalInfo;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/***
 * The place class is used to represent the logic for a place in a PetriNet.
 */
public class Place implements Serializable, TextDescribable {

    private static final long serialVersionUID = -8990623494892563264L;

    private final String id;
    private final Set<Transition> inputTransitions;
    private final Set<Transition> outputTransitions;
    private int numTokens;
    private boolean isFinal;

    private PlaceAdditionalInfo additionalInfo;

    public Place() {
        this(UUID.randomUUID().toString());
    }

    public Place(String id) {
        this.id = id;
        this.inputTransitions = new HashSet<>();
        this.outputTransitions = new HashSet<>();
        this.numTokens = 0;
        this.additionalInfo = new PlaceAdditionalInfo();
        this.isFinal = false;
    }

    public static Place from(String stringRepresentation) {
        Place place = new Place();
        String[] transitions = stringRepresentation.split(" \\| ");
        for (String tr : transitions[0].split(" ")) {
            place.addInputTransition(new Transition(tr, false));
        }
        for (String tr : transitions[1].split(" ")) {
            place.addOutputTransition(new Transition(tr, false));
        }
        return place;
    }

    public boolean isInputTransition(Transition transition) {
        return inputTransitions.contains(transition);
    }

    public boolean isOutputTransition(Transition transition) {
        return outputTransitions.contains(transition);
    }

    public boolean isInputTransitionLabel(String label) {
        return inputTransitions.stream().map(Transition::getLabel).collect(Collectors.toSet()).contains(label);
    }

    public boolean isOutputTransitionLabel(String label) {
        return outputTransitions.stream().map(Transition::getLabel).collect(Collectors.toSet()).contains(label);
    }

    public Set<Transition> getInputTransitions() {
        return inputTransitions;
    }

    public Set<Transition> getOutputTransitions() {
        return outputTransitions;
    }

    public String getId() {
        return id;
    }

    public void addInputTransition(Transition transition) {
        inputTransitions.add(transition);
    }

    public void addOutputTransition(Transition transition) {
        outputTransitions.add(transition);
    }

    public int getNumTokens() {
        return numTokens;
    }

    public void setNumTokens(int numTokens) {
        this.numTokens = numTokens;
    }

    public boolean isSelfLoop(String label) {
        Set<Transition> selfLoops = Sets.intersection(this.inputTransitions, this.outputTransitions);
        return selfLoops.stream().map(Transition::getLabel).collect(Collectors.toSet()).contains(label);
    }

    @Override
    public String getShortString() {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        List<String> trStr = inputTransitions.stream().map(Transition::getLabel).sorted().collect(Collectors.toList());
        for (String tr : trStr)
            sb.append(tr).append(" ");
        sb.append("|");

        trStr = outputTransitions.stream().map(Transition::getLabel).sorted().collect(Collectors.toList());
        for (String tr : trStr)
            sb.append(" ").append(tr);
        sb.append(")");

        return sb.toString();
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (getClass() != obj.getClass())
//            return false;
//
//        Place place = (Place) obj;
//        return this.inputTransitions.equals(place.inputTransitions)
//                && this.outputTransitions.equals(place.outputTransitions)
//                && this.id.equals(place.id);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return Objects.equals(inputTransitions, place.inputTransitions) &&
                Objects.equals(outputTransitions, place.outputTransitions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputTransitions, outputTransitions);
    }

    @Override
    public String toString() {
        return "Place: " + id + "\n" + this.getShortString() + "\n";
    }

    public boolean covers(Place p) {
        return this.inputTransitions.containsAll(p.inputTransitions) && this.outputTransitions.containsAll(p.outputTransitions);
    }

    public void removeTransition(Transition transition, boolean asInput) {
        if (asInput) {
            this.inputTransitions.remove(transition);
            return;
        }
        this.outputTransitions.remove(transition);
    }

    public void removeTransitions(String transitionLabel, boolean asInput) {
        Set<Transition> transitions = asInput ? this.inputTransitions : this.outputTransitions;
        transitions = transitions
                .stream()
                .filter(t -> t.getLabel().equals(transitionLabel))
                .collect(Collectors.toSet());
        for (Transition t : transitions)
            this.removeTransition(t, asInput);
    }

    public void removeTransitions(String transitionLabel) {
        this.removeTransitions(transitionLabel, true);
        this.removeTransitions(transitionLabel, false);
    }

    public PlaceAdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(PlaceAdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    public Set<Transition> getSilentTransitions(boolean isInput) {
        return isInput ? this.getInputTransitions().stream().filter(Transition::isInvisible).collect(Collectors.toSet()) :
                this.getOutputTransitions().stream().filter(Transition::isInvisible).collect(Collectors.toSet());
    }
}
