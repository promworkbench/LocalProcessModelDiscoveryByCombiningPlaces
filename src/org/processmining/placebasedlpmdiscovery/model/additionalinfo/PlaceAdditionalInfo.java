package org.processmining.placebasedlpmdiscovery.model.additionalinfo;

import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Place additional info at the moment completely taken from eST miner
 */
public class PlaceAdditionalInfo implements Serializable {

    private static final long serialVersionUID = -8322911097736437096L;

    private final Place place;
    private Map<Passage, Integer> passageUsage;

    public PlaceAdditionalInfo(Place place) {
        this.place = place;
    }

    public void writePassageUsage(LEFRMatrix lefrMatrix) {
        passageUsage = new HashMap<>();
        for (Transition inTr : place.getInputTransitions()) {
            for (Transition outTr : place.getOutputTransitions()) {
                String in = inTr.getLabel();
                String out = outTr.getLabel();
                passageUsage.put(new Passage(in, out), lefrMatrix.get(in, out));
            }
        }
    }

    public Map<Passage, Integer> getPassageUsage() {
        return passageUsage;
    }

}
