package org.processmining.lpms.quality.alignments.tax;

import nl.tue.astar.AStarException;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryNaiveImpl;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.PNAlignments;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.petrinet.replayer.matchinstances.InfoObjectConst;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.AllSyncReplayResult;

import java.util.*;

public class TaxPNAlignments implements PNAlignments {
    /**
     * evCost is 0 for every event class above, so an alignment made entirely of log moves is
     * always tied for optimal cost - it never uses the model at all, so it's not a meaningful
     * representative for LPM quality computation. Drop it from each trace's alignment set.
     */
    @SuppressWarnings("unchecked")
    private static void removeAlignmentsWithoutSyncMoves(PNMatchInstancesRepResult result) {
        for (AllSyncReplayResult r : result) {
            List<List<StepTypes>> stepTypesLst = r.getStepTypesLst();
            List<List<Object>> nodeInstanceLst = r.getNodeInstanceLst();
            List<Integer> numRepresented = r.getInfoObject() == null ? null
                    : (List<Integer>) r.getInfoObject().get(InfoObjectConst.NUMREPRESENTEDALIGNMENT);
            for (int i = stepTypesLst.size() - 1; i >= 0; i--) {
                if (!stepTypesLst.get(i).contains(StepTypes.LMGOOD)) {
                    stepTypesLst.remove(i);
                    nodeInstanceLst.remove(i);
                    if (numRepresented != null && i < numRepresented.size()) {
                        numRepresented.remove(i);
                    }
                }
            }
        }
    }

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
                evCost.put(ec, 0); // penalize skipping events the LPM knows about
            } else {
                evCost.put(ec, 0); // free to skip events outside LPM alphabet
            }
        }

        TransEvClassMapping transEvMapping = instantiateTransEventMappingEqualName(
                eventClasses, dummy, pn);

        // [0] mapTransition2Cost [1] maxNumOfStates [2] mapEventClass2Cost [3] numOfSamples
        Object[] params = new Object[]{transCost, 200000, evCost, 100};

        // Run. TieAwareNBestAlignmentsAlg is a drop-in replacement for
        // NBestOptAlignmentsNoModelMoveGraphSamplingAlg that recovers optimal alignments the
        // upstream search silently drops on ties - see TieCapturingSamplingThread.
        TieAwareNBestAlignmentsAlg alg = new TieAwareNBestAlignmentsAlg();

        PNMatchInstancesRepResult result = alg.replayLog(null, pn, apn.getInitialMarking(),
                apn.getFinalMarkings().stream().findFirst().get(), log, transEvMapping, params);
        removeAlignmentsWithoutSyncMoves(result);
        return result;
    }

    @Override
    public PNMatchInstancesRepResult compute(AcceptingPetriNet apn, XTrace trace) throws AStarException {
        XFactory factory = new XFactoryNaiveImpl();
        XLog log = factory.createLog();
        log.add(trace);
        return compute(apn, log);
    }

    /**
     * Compute a transition to event class mapping based on the name (name
     * classifier not considering lifecycle).
     *
     * @param eventClasses Event classes to which transitions should be mapped.
     * @param dummy        Dummy event class to which silent transitions are mapped.
     * @param pn           Petri net whose transition should be mapped
     * @return Mapping from Transition to Event Class
     */
    public static TransEvClassMapping instantiateTransEventMappingEqualName(
            XEventClasses eventClasses, XEventClass dummy, Petrinet pn) {
        TransEvClassMapping mapping;
        mapping = new TransEvClassMapping(eventClasses.getClassifier(), dummy);
        int sucessfulVisMapping = 0;
        int visTransitions = 0;
        for (Transition t : pn.getTransitions()) {
            if (t.isInvisible()) {
                mapping.put(t, dummy);
            } else {
                XEventClass eventClass = eventClasses.getByIdentity(t.getLabel());
                if (eventClass != null) {
                    mapping.put(t, eventClass);
                    sucessfulVisMapping++;
                } else {
                    System.out.println(t.getLabel());
                }
                visTransitions++;
            }
        }

        if (sucessfulVisMapping != visTransitions) {
            throw new IllegalArgumentException("Some labels of visible transitions do not exist in the event log.");
        }

        return mapping;

    }
}
