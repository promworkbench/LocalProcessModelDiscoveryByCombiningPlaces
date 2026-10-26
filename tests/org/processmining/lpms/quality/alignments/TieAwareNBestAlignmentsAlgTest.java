package org.processmining.lpms.quality.alignments;

import nl.tue.astar.AStarException;
import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.classification.XEventClasses;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.info.impl.XLogInfoImpl;
import org.deckfour.xes.model.XLog;
import org.junit.Test;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.tax.TaxPNAlignments;
import org.processmining.lpms.quality.alignments.tax.TieAwareNBestAlignmentsAlg;
import org.processmining.mockobjects.MockLPMs;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Same setup as
 * TaxPNAlignmentsTest#givenTraceWithRepeatedPattern_whenCompute_thenAllFourOptimalAlignmentsAreReturned
 * and DebugNBestAlignmentsTest, but run through {@link TieAwareNBestAlignmentsAlg} (the patched
 * algorithm) instead of the stock NBestOptAlignmentsNoModelMoveGraphSamplingAlg. The unpatched
 * algorithm only ever returns 4 of the 6 valid optimal alignments for trace a,b,c,a,b,c against
 * MockLPMs.getSequenceLPM_abc() - always missing sync-triples (0,1,5) and (3,4,5) - see
 * memory/nbest_alignment_tie_collapse.md for the full root-cause trace. This test asserts the
 * patch recovers all 6.
 */
public class TieAwareNBestAlignmentsAlgTest {

    @Test
    public void givenTraceWithRepeatedPattern_whenCompute_thenAllSixOptimalAlignmentsAreReturned() throws AStarException {
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
        Object[] params = new Object[]{transCost, 200000, evCost, 10};

        TieAwareNBestAlignmentsAlg alg = new TieAwareNBestAlignmentsAlg();
        PNMatchInstancesRepResult result = alg.replayLog(null, pn, apn.getInitialMarking(),
                apn.getFinalMarkings().stream().findFirst().get(), log, transEvMapping, params);

        System.out.println("=== RESULT: " + result.size() + " trace-result group(s) ===");
        result.forEach(r -> {
            System.out.println("alignments found: " + r.getStepTypesLst().size());
            r.getStepTypesLst().forEach(steps -> System.out.println("  " + steps));
        });

        assertEquals(1, result.size());
        assertEquals(6, result.first().getStepTypesLst().size());
    }
}
