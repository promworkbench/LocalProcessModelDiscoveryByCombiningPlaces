package org.processmining.lpms.quality.alignments;

import nl.tue.astar.AStarException;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.Test;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.tax.TaxPNAlignments;
import org.processmining.lpms.quality.alignments.tax.TieCapturingSamplingThread;
import org.processmining.mockobjects.MockLPMs;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;

import java.util.*;

/**
 * Reproduces the exact setup of
 * TaxPNAlignmentsTest#givenTraceWithRepeatedPattern_whenCompute_thenAllFourOptimalAlignmentsAreReturned,
 * but runs it through {@link DebuggingNBestAlg} so the search prints every visited state,
 * traversed edge, and found final record to stdout.
 */
public class DebugNBestAlignmentsTest {

    @Test
    public void debugRepeatedPatternTrace() throws AStarException {
        LocalProcessModel lpm = MockLPMs.getSequenceLPM_abc();
        AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
        Petrinet pn = apn.getNet();

        // trace positions: a0, b1, c2, a3, b4, c5
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c", "a", "b", "c"))).getOriginalLog();

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
        for (Transition t : pn.getTransitions()) {
            transCost.put(t, t.isInvisible() ? 0 : Integer.MAX_VALUE);
        }
        Map<XEventClass, Integer> evCost = new HashMap<>();
        for (XEventClass ec : eventClasses.getClasses()) {
            evCost.put(ec, 0);
        }

        TransEvClassMapping transEvMapping = TaxPNAlignments.instantiateTransEventMappingEqualName(
                eventClasses, dummy, pn);

        // [0] mapTransition2Cost [1] maxNumOfStates [2] mapEventClass2Cost [3] numOfSamples
        Object[] params = new Object[]{transCost, 200000, evCost, 100};

        DebuggingNBestAlg alg = new DebuggingNBestAlg();
        PNMatchInstancesRepResult result = alg.replayLog(null, pn, apn.getInitialMarking(),
                apn.getFinalMarkings().stream().findFirst().get(), log, transEvMapping, params);

        System.out.println("=== RESULT: " + result.size() + " trace-result group(s) ===");
        result.forEach(r -> {
            System.out.println("alignments found: " + r.getStepTypesLst().size());
            r.getStepTypesLst().forEach(steps -> System.out.println("  " + steps));
        });
    }

    /**
     * Probes whether a SINGLE winning alignment chain can pick up recorded ties at more than one
     * block boundary - the "double-tie-same-chain" gap documented in
     * memory/nbest_alignment_tie_collapse.md, which {@link TieCapturingSamplingThread#buildAlternates}
     * cannot recover (it grafts one alternate per tie independently, never combinations of two).
     * A two-block repeat ("a,b,c,a,b,c") only has one block boundary (k=3) besides the terminal
     * one (k=6), so it can't exhibit this; a three-block repeat has two interior boundaries
     * (k=3, k=6) plus the terminal one (k=9), giving a winning chain more chances to pass through
     * two independently-recorded ties. Runs through the real production path
     * (PNAlignments.tax()), so the "MULTI-TIE CHAIN" diagnostic in buildAlternates will print to
     * stdout if and only if this trace actually triggers the gap.
     */
    @Test
    public void debugTripleRepeatedPatternTrace() throws AStarException {
        LocalProcessModel lpm = MockLPMs.getSequenceLPM_abc();
        AcceptingPetriNet apn = LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);

        // trace positions: a0,b1,c2, a3,b4,c5, a6,b7,c8
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(Collections.singletonList(
                Arrays.asList("a", "b", "c", "a", "b", "c", "a", "b", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        PNMatchInstancesRepResult result = PNAlignments.tax().compute(apn, trace);

        System.out.println("=== RESULT: " + result.iterator().next().getStepTypesLst().size() + " alignment(s) ===");
        result.iterator().next().getStepTypesLst().forEach(steps -> System.out.println("  " + steps));
    }
}
