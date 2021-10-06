package org.processmining.placebasedlpmdiscovery.model;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.PlaceAdditionalInfo;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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
        this.additionalInfo = new PlaceAdditionalInfo(this);
        this.isFinal = false;
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
        for (Transition tr : this.inputTransitions)
            sb.append(tr.getLabel()).append(" ");
        sb.append("|");
        for (Transition tr : this.outputTransitions)
            sb.append(" ").append(tr.getLabel());
        sb.append(")");

        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;

        Place place = (Place) obj;
        return this.inputTransitions.equals(place.inputTransitions)
                && this.outputTransitions.equals(place.outputTransitions);
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
