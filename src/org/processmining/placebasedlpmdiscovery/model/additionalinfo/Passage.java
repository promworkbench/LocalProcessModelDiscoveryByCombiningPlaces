package org.processmining.placebasedlpmdiscovery.model.additionalinfo;

import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.io.Serializable;
import java.util.Objects;

public class Passage implements Serializable {

    private static final long serialVersionUID = 8617832102315118677L;

    private final Transition inTr;
    private final Transition outTr;

    public Passage(Transition inTr, Transition outTr) {
        this.inTr = inTr;
        this.outTr = outTr;
    }

    public Transition getInTr() {
        return inTr;
    }

    public Transition getOutTr() {
        return outTr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passage passage = (Passage) o;
        return Objects.equals(inTr, passage.inTr) && Objects.equals(outTr, passage.outTr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inTr, outTr);
    }

    @Override
    public String toString() {
        return inTr + " -> " + outTr;
    }
}
