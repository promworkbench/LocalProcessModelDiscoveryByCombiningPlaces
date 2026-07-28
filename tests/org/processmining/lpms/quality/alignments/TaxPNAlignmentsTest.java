package org.processmining.lpms.quality.alignments;

import nl.tue.astar.AStarException;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.Assert;
import org.junit.Test;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.mockobjects.MockLPMs;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.AllSyncReplayResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TaxPNAlignmentsTest {

    private static AcceptingPetriNet sequenceAbcNet() {
        LocalProcessModel lpm = MockLPMs.getSequenceLPM_abc();
        return LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
    }

    @Test
    public void givenPerfectlyFittingTrace_whenComputeOnTrace_thenAllStepsAreSynchronousMoves() throws AStarException {
        // set input
        AcceptingPetriNet apn = sequenceAbcNet();
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        // act
        PNMatchInstancesRepResult result = PNAlignments.tax().compute(apn, trace);

        // set expected result
        List<StepTypes> expectedSteps = Arrays.asList(StepTypes.LMGOOD, StepTypes.LMGOOD, StepTypes.LMGOOD);

        // test
        Assert.assertEquals(1, result.size());
        Assert.assertEquals(expectedSteps, result.iterator().next().getStepTypesLst().get(0));
    }

    @Test
    public void givenPerfectlyFittingTrace_whenComputeOnLogVsOnTrace_thenSameAlignment() throws AStarException {
        // set input
        AcceptingPetriNet apn = sequenceAbcNet();
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        // act
        PNMatchInstancesRepResult logResult = PNAlignments.tax().compute(apn, log);
        PNMatchInstancesRepResult traceResult = PNAlignments.tax().compute(apn, trace);

        // test
        Assert.assertEquals(
                logResult.iterator().next().getStepTypesLst().get(0),
                traceResult.iterator().next().getStepTypesLst().get(0));
    }

    @Test
    public void givenLogWithNonFittingTrace_whenCompute_thenTraceHasNonSynchronousStep() throws AStarException {
        // set input
        AcceptingPetriNet apn = sequenceAbcNet();
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(Arrays.asList(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("a", "c") // missing "b"
        )).getOriginalLog();

        // act
        PNMatchInstancesRepResult result = PNAlignments.tax().compute(apn, log);

        // set expected result
        AllSyncReplayResult nonFittingResult = result.stream()
                .filter(r -> r.getTraceIndex().contains(1))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No alignment found for the non-fitting trace"));

        // test
        Assert.assertTrue(nonFittingResult.getStepTypesLst().get(0).stream()
                .anyMatch(step -> step != StepTypes.LMGOOD));
    }

    @Test
    public void givenTraceWithRepeatedPattern_whenCompute_thenAllFourOptimalAlignmentsAreReturned() throws AStarException {
        // set input
        AcceptingPetriNet apn = sequenceAbcNet();
        // trace positions: a0, b1, c2, a3, b4, c5
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c", "a", "b", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        // act
        PNMatchInstancesRepResult result = PNAlignments.tax().compute(apn, trace);

        // set expected result: every increasing (a, b, c) index triple is a valid, equally optimal occurrence
        Set<List<StepTypes>> expectedAlignments = new HashSet<>(Arrays.asList(
                stepsWithSyncAt(),
                stepsWithSyncAt(0, 1, 2),
                stepsWithSyncAt(0, 1, 5),
                stepsWithSyncAt(0, 4, 5),
                stepsWithSyncAt(3, 4, 5),
                stepsWithSyncAt(0, 1, 2, 3, 4, 5)));

        // test
        Assert.assertEquals(result.first().getStepTypesLst().toString(),
                expectedAlignments.size(), result.first().getStepTypesLst().size());
        Set<List<StepTypes>> actualAlignments = result.stream()
                .flatMap(r -> r.getStepTypesLst().stream())
                .collect(Collectors.toSet());
        Assert.assertEquals(expectedAlignments, actualAlignments);
    }

    private static List<StepTypes> stepsWithSyncAt(int... syncPositions) {
        Set<Integer> syncIndices = new HashSet<>();
        for (int pos : syncPositions) {
            syncIndices.add(pos);
        }

        List<StepTypes> steps = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            steps.add(syncIndices.contains(i) ? StepTypes.LMGOOD : StepTypes.L);
        }
        return steps;
    }
}