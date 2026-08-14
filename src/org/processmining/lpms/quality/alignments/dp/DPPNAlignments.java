package org.processmining.lpms.quality.alignments.dp;

import nl.tue.astar.AStarException;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.PNAlignments;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;

public class DPPNAlignments implements PNAlignments {

    @Override
    public PNMatchInstancesRepResult compute(AcceptingPetriNet apn, XLog log) throws AStarException {
        return null;
    }

    @Override
    public PNMatchInstancesRepResult compute(AcceptingPetriNet apn, XTrace trace) throws AStarException {
        return null;
    }
}