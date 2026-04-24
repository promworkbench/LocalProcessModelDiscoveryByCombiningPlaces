package org.processmining.lpms.quality.alignments;

import nl.tue.astar.AStarException;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpm.adjustedalignments.NBestOptAlignmentsNoModelMoveGraphSamplingAlg;
import org.processmining.lpms.quality.alignments.temp.PNLogConnector;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TaxPNAlignments implements PNAlignments {
    @Override
    public PNMatchInstancesRepResult compute(AcceptingPetriNet apn, XLog log) throws AStarException {
        Petrinet pn = apn.getNet();

        XEventClasses eventClasses = XLogInfoImpl.create(log, new XEventNameClassifier())
                .getEventClasses(new XEventNameClassifier());
        XEventClass dummy = new XEventClass("", -1);

        Set<String> lpmAlphabet = new HashSet<>();
        for (Transition t : pn.getTransitions()) {
            if (!t.isInvisible()) {
                lpmAlphabet.add(t.getLabel());
            }
        }

        Map<Transition, Integer> transCost = new HashMap<>();
        for (Transition t : pn.getTransitions())
            transCost.put(t, t.isInvisible() ? 0 : Integer.MAX_VALUE);
        Map<XEventClass, Integer> evCost = new HashMap<>();
        for (XEventClass ec : eventClasses.getClasses()) {
            if (lpmAlphabet.contains(ec.getId())) {
                evCost.put(ec, 5); // penalize skipping events the LPM knows about
            } else {
                evCost.put(ec, 0); // free to skip events outside LPM alphabet
            }
        }

        TransEvClassMapping transEvMapping = PNLogConnector.instantiateTransEventMappingEqualName(
                eventClasses, dummy, pn);

        Object[] params = new Object[] { transCost, 200000, evCost, 1 };

        // Run
        NBestOptAlignmentsNoModelMoveGraphSamplingAlg alg =
                new NBestOptAlignmentsNoModelMoveGraphSamplingAlg();

        return alg.replayLog(null, pn, apn.getInitialMarking(), apn.getFinalMarkings().stream().findFirst().get(),
                log, transEvMapping, params);
    }
}
