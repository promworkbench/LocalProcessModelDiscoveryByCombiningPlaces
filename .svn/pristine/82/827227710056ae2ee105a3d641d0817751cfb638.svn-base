package org.processmining.placebasedlpmdiscovery.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * The Transition class is used to represent the logic for a transition in a PetriNet
 */
public class Transition implements Serializable {
    private static final long serialVersionUID = -1762861841941171313L;
    private String id;
    private String label;
    private boolean invisible;

    public Transition() {
        this.id = UUID.randomUUID().toString();
    }

    public Transition(String label, boolean invisible) {
        this();
        this.label = label;
        this.invisible = invisible;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
    }

    @Override
    public boolean equals(Object obj) {
        if (getClass() != obj.getClass())
            return false;

        Transition transition = (Transition) obj;
        return this.label.equals(transition.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(label);
    }

    @Override
    public String toString() {
        return this.id + ": " + this.label;
    }
}
