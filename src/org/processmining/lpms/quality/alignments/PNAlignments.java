package org.processmining.lpms.quality.alignments;

import nl.tue.astar.AStarException;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.tax.TaxPNAlignments;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;

public interface PNAlignments {

    static PNAlignments tax() {
        return new TaxPNAlignments();
    }

    PNMatchInstancesRepResult compute(AcceptingPetriNet apn, XLog log) throws AStarException;

    PNMatchInstancesRepResult compute(AcceptingPetriNet apn, XTrace trace) throws AStarException;
}
