package org.processmining.placebasedlpmdiscovery.model.additionalinfo;

import java.io.Serializable;
import java.util.Objects;

public class Passage implements Serializable {

    private static final long serialVersionUID = 8617832102315118677L;

    private final String inTr;
    private final String outTr;

    public Passage(String inTr, String outTr) {
        this.inTr = inTr;
        this.outTr = outTr;
    }

    public String getInTr() {
        return inTr;
    }

    public String getOutTr() {
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
