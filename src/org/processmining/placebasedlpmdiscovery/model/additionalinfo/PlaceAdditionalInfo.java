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

    private Map<Passage, Integer> passageUsage; // TODO: This is problematic for Gson

    public void writePassageUsage(LEFRMatrix lefrMatrix, Place place) {
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
