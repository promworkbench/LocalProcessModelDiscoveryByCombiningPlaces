package org.processmining.lpms.quality.alignments.dp;

import nl.tue.astar.AStarException;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.Assert;
import org.junit.Test;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.PNAlignments;
import org.processmining.mockobjects.MockLPMs;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;

import java.util.*;
import java.util.stream.Collectors;

public class DPPNAlignmentsTest {

    private static final PNAlignments ALIGNMENTS = new DPPNAlignments();
    private static final int DETERMINISM_RUNS = 1000;

    private static AcceptingPetriNet sequenceAbcNet() {
        LocalProcessModel lpm = MockLPMs.getSequenceLPM_abc();
        return LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
    }

    private static AcceptingPetriNet choiceNet() {
        LocalProcessModel lpm = MockLPMs.getChoiceLPM_aXbc();
        return LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
    }

    private static AcceptingPetriNet concurrentNet() {
        LocalProcessModel lpm = MockLPMs.getConcurrentLPM_aANDbc();
        return LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
    }

    // a single initial token is shared by a and b (only one can ever fire), but c requires a
    // token from both of their output places at once - so c, and the empty final marking, can
    // never be reached
    private static AcceptingPetriNet choiceIntoUnreachableJoinNet() {
        Place choicePlace = Place.from("| a, b");
        choicePlace.setNumTokens(1);

        LocalProcessModel lpm = new LocalProcessModel();
        lpm.addPlace(choicePlace);
        lpm.addPlace(Place.from("a | c"));
        lpm.addPlace(Place.from("b | c"));
        return LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
    }

    // a, then a choice between "b" (visible) and an invisible "skip" transition that lets a trace
    // omit "b" for free, then c - both branches feed the same place before c
    private static AcceptingPetriNet invisibleSkipNet() {
        Place beforeChoice = new Place();
        beforeChoice.addInputTransition(new Transition("a", false));
        beforeChoice.addOutputTransition(new Transition("b", false));
        beforeChoice.addOutputTransition(new Transition("skip", true));

        Place afterChoice = new Place();
        afterChoice.addInputTransition(new Transition("b", false));
        afterChoice.addInputTransition(new Transition("skip", true));
        afterChoice.addOutputTransition(new Transition("c", false));

        LocalProcessModel lpm = new LocalProcessModel();
        lpm.addPlace(beforeChoice);
        lpm.addPlace(afterChoice);
        return LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm);
    }

    private static List<StepTypes> stepsWithSyncAt(int length, int... syncPositions) {
        Set<Integer> syncIndices = new HashSet<>();
        for (int pos : syncPositions) {
            syncIndices.add(pos);
        }

        List<StepTypes> steps = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            steps.add(syncIndices.contains(i) ? StepTypes.LMGOOD : StepTypes.L);
        }
        return steps;
    }

    @Test
    public void givenPerfectlyFittingTrace_whenComputeOnLogVsOnTrace_thenSameAlignment() throws AStarException {
        // set input
        AcceptingPetriNet apn = sequenceAbcNet();
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        // act
        PNMatchInstancesRepResult logResult = ALIGNMENTS.compute(apn, log);
        PNMatchInstancesRepResult traceResult = ALIGNMENTS.compute(apn, trace);

        // test
        Assert.assertEquals(
                logResult.iterator().next().getStepTypesLst().get(0),
                traceResult.iterator().next().getStepTypesLst().get(0));
    }

    @Test
    public void givenPerfectlyFittingTrace_whenComputeOnTrace_thenAllStepsAreSynchronousMoves() throws AStarException {
        // set input
        AcceptingPetriNet apn = sequenceAbcNet();
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        // act
        PNMatchInstancesRepResult result = ALIGNMENTS.compute(apn, trace);

        // set expected result
        List<StepTypes> expectedSteps = Arrays.asList(StepTypes.LMGOOD, StepTypes.LMGOOD, StepTypes.LMGOOD);

        // test
        Assert.assertEquals(1, result.iterator().next().getStepTypesLst().size());
        Assert.assertEquals(expectedSteps, result.iterator().next().getStepTypesLst().get(0));
    }

    @Test
    public void givenLogWithNonFittingTrace_whenCompute_thenTraceHasNonSynchronousStep() throws AStarException {
        // set input
        AcceptingPetriNet apn = sequenceAbcNet();
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(Arrays.asList(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("a", "n", "b", "c") // extra "n"
        )).getOriginalLog();

        // act
        PNMatchInstancesRepResult result = ALIGNMENTS.compute(apn, log);

        // set expected result
        Set<List<StepTypes>> expectedAlignments = new HashSet<>(Arrays.asList(
                stepsWithSyncAt(3, 0, 1, 2),
                stepsWithSyncAt(4, 0, 2, 3)));

        // test
        Set<List<StepTypes>> actualAlignments = result.stream()
                .flatMap(r -> r.getStepTypesLst().stream())
                .collect(Collectors.toSet());
        Assert.assertEquals(expectedAlignments, actualAlignments);
    }

    @Test
    public void givenTraceWithRepeatedPattern_whenCompute_thenAllFiveOptimalAlignmentsAreReturned() throws AStarException {
        // set input
        AcceptingPetriNet apn = sequenceAbcNet();
        // trace positions: a0, b1, c2, a3, b4, c5
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c", "a", "b", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        // act
        PNMatchInstancesRepResult result = ALIGNMENTS.compute(apn, trace);

        // set expected result: every increasing (a, b, c) index triple is a valid, equally optimal
        // occurrence. The all-log (zero-sync) alignment is also tied for optimal cost, but
        // PNAlignments.tax() filters it out since it never uses the model at all.
        Set<List<StepTypes>> expectedAlignments = new HashSet<>(Arrays.asList(
                stepsWithSyncAt(6, 0, 1, 2),
                stepsWithSyncAt(6, 0, 1, 5),
                stepsWithSyncAt(6, 0, 4, 5),
                stepsWithSyncAt(6, 3, 4, 5)));

        // test
        Assert.assertEquals(result.first().getStepTypesLst().toString(),
                expectedAlignments.size(), result.first().getStepTypesLst().size());
        Set<List<StepTypes>> actualAlignments = result.stream()
                .flatMap(r -> r.getStepTypesLst().stream())
                .collect(Collectors.toSet());
        Assert.assertEquals(expectedAlignments, actualAlignments);
    }

    @Test
    public void givenTraceWithThreeRepeatedPatterns_whenCompute_thenAllTwentyOneOptimalAlignmentsAreReturned()
            throws AStarException {
        // set input
        AcceptingPetriNet apn = sequenceAbcNet();
        // trace positions: a0, b1, c2, a3, b4, c5, a6, b7, c8
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                        Collections.singletonList(Arrays.asList("a", "b", "c", "a", "b", "c", "a", "b", "c")))
                .getOriginalLog();
        XTrace trace = log.get(0);

        // act
        PNMatchInstancesRepResult result = ALIGNMENTS.compute(apn, trace);

        // set expected result: every combination of a-, b- and c-occurrences that forms a valid
        // increasing (a before b before c, per matched cycle) sync selection is equally optimal -
        // one cycle synced (10 combinations), two cycles synced (10 combinations), or all three
        // cycles synced (1 combination) - for 21 total. As with the two-repetition case above, the
        // all-log (zero-sync) alignment is tied for optimal cost too but is filtered out by
        // PNAlignments.tax() since it never uses the model at all.
        Set<List<StepTypes>> expectedAlignments = new HashSet<>(Arrays.asList(
                // one cycle synced
                stepsWithSyncAt(9, 0, 1, 2),
                stepsWithSyncAt(9, 0, 1, 5),
                stepsWithSyncAt(9, 0, 1, 8),
                stepsWithSyncAt(9, 0, 4, 5),
                stepsWithSyncAt(9, 0, 4, 8),
                stepsWithSyncAt(9, 0, 7, 8),
                stepsWithSyncAt(9, 3, 4, 5),
                stepsWithSyncAt(9, 3, 4, 8),
                stepsWithSyncAt(9, 3, 7, 8),
                stepsWithSyncAt(9, 6, 7, 8)));

        // test
        Assert.assertEquals(result.first().getStepTypesLst().toString(),
                expectedAlignments.size(), result.first().getStepTypesLst().size());
        Set<List<StepTypes>> actualAlignments = result.stream()
                .flatMap(r -> r.getStepTypesLst().stream())
                .collect(Collectors.toSet());
        Assert.assertEquals(expectedAlignments, actualAlignments);
    }

    @Test
    public void givenChoiceBetweenBranches_whenCompute_thenBothBranchAlignmentsAreReturned() throws AStarException {
        // set input
        AcceptingPetriNet apn = choiceNet();
        // trace positions: a0, b1, c2 - the model's single place only holds one token from "a",
        // so at most one of b/c can be a synchronous move
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        // act
        PNMatchInstancesRepResult result = ALIGNMENTS.compute(apn, trace);

        // set expected result: syncing "a" with either "b" or "c" is equally optimal, forcing the
        // other branch's event to be a log move. The all-log (zero-sync) alignment is tied for
        // optimal cost too but is filtered out by PNAlignments.tax(), as in the sequence tests above
        Set<List<StepTypes>> expectedAlignments = new HashSet<>(Arrays.asList(
                stepsWithSyncAt(3, 0, 1),
                stepsWithSyncAt(3, 0, 2)));

        // test
        Set<List<StepTypes>> actualAlignments = result.stream()
                .flatMap(r -> r.getStepTypesLst().stream())
                .collect(Collectors.toSet());
        Assert.assertEquals(expectedAlignments, actualAlignments);
    }

    @Test
    public void givenConcurrentBranches_whenComputeOnEitherEventOrder_thenAllStepsAreSynchronousMoves()
            throws AStarException {
        // set input
        AcceptingPetriNet apn = concurrentNet();
        // b and c each only depend on a, with no place ordering them relative to each other, so
        // either trace order should align fully synchronously
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(Arrays.asList(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("a", "c", "b"))).getOriginalLog();

        List<StepTypes> expectedSteps = Arrays.asList(StepTypes.LMGOOD, StepTypes.LMGOOD, StepTypes.LMGOOD);

        // act & test
        for (XTrace trace : log) {
            PNMatchInstancesRepResult result = ALIGNMENTS.compute(apn, trace);
            Assert.assertEquals(1, result.iterator().next().getStepTypesLst().size());
            Assert.assertEquals(expectedSteps, result.iterator().next().getStepTypesLst().get(0));
        }
    }

    @Test
    public void givenTraceSkippingOptionalActivityViaInvisibleTransition_whenCompute_thenAllStepsAreSynchronousMoves()
            throws AStarException {
        // set input
        AcceptingPetriNet apn = invisibleSkipNet();
        // "b" is skipped by silently firing the invisible "skip" transition instead, so both "a"
        // and "c" should be able to synchronize even though the model never fires "b"
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        // act
        PNMatchInstancesRepResult result = ALIGNMENTS.compute(apn, trace);

        // set expected result: the invisible transition fires for free and does not appear in the
        // reported alignment, so both trace events synchronize with the model
        List<StepTypes> expectedSteps = Arrays.asList(StepTypes.LMGOOD, StepTypes.LMGOOD);

        // test
        Assert.assertEquals(1, result.iterator().next().getStepTypesLst().size());
        Assert.assertEquals(expectedSteps, result.iterator().next().getStepTypesLst().get(0));
    }

    @Test
    public void givenTraceSkippingOptionalActivityViaInvisibleTransition_whenComputeRepeatedly_thenResultIsDeterministic()
            throws AStarException {
        // set input: same model/trace as
        // givenTraceSkippingOptionalActivityViaInvisibleTransition_whenCompute_thenAllStepsAreSynchronousMoves
        AcceptingPetriNet apn = invisibleSkipNet();
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        // act & test: every run should produce the same single optimal alignment
        List<StepTypes> firstAlignment = null;
        for (int run = 0; run < DETERMINISM_RUNS; run++) {
            List<List<StepTypes>> alignments = ALIGNMENTS.compute(apn, trace).iterator().next().getStepTypesLst();
            Assert.assertEquals("run " + run + " of " + DETERMINISM_RUNS, 1, alignments.size());
            if (firstAlignment == null) {
                firstAlignment = alignments.get(0);
            } else {
                Assert.assertEquals("run " + run + " of " + DETERMINISM_RUNS, firstAlignment, alignments.get(0));
            }
        }
    }

    @Test
    public void givenLpmAndLogFilesAndTraceIndex_whenCompute_thenAlignmentsAreComputedForThatPair() throws Exception {
        // TODO: point these at the lpm/log files and trace index to debug.
        String lpmFile = "./data/test/lpms/tax/artificialBig/tax_artificialBig_23.pnml";
        String logFile = "./data/logs/artificialBig.xes";
        int traceIndex = 2;

        // set input
        AcceptingPetriNet apn = PlaceUtils.extractAcceptingPetriNet(lpmFile);
        XLog log = LogUtils.readLogFromFile(logFile);
        XTrace trace = log.get(traceIndex);

        // act
        PNMatchInstancesRepResult result = ALIGNMENTS.compute(apn, trace);

        // report so the computed alignment(s) can be inspected manually
        List<List<StepTypes>> alignments = result.iterator().next().getStepTypesLst();
        System.out.printf("Computed %d optimal alignment(s) for trace %d of %s against %s:%n",
                alignments.size(), traceIndex, logFile, lpmFile);
        alignments.forEach(System.out::println);
    }

    @Test
    public void givenLpmAndLogFilesAndTraceIndex_whenComputeRepeatedly_thenResultIsDeterministic() throws Exception {
        // set input: same lpm/log/trace index as
        // givenLpmAndLogFilesAndTraceIndex_whenCompute_thenAlignmentsAreComputedForThatPair
        String lpmFile = "./data/test/lpms/tax/artificialBig/tax_artificialBig_37.pnml";
        String logFile = "./data/logs/artificialBig.xes";
        int traceIndex = 0;

        AcceptingPetriNet apn = PlaceUtils.extractAcceptingPetriNet(lpmFile);
        XLog log = LogUtils.readLogFromFile(logFile);
        XTrace trace = log.get(traceIndex);

        // act & test: every run should produce the same set of optimal alignments
        Set<List<StepTypes>> firstAlignments = null;
        for (int run = 0; run < DETERMINISM_RUNS; run++) {
            Set<List<StepTypes>> alignments = new HashSet<>(
                    ALIGNMENTS.compute(apn, trace).iterator().next().getStepTypesLst());
            if (firstAlignments == null) {
                firstAlignments = alignments;
            } else {
                Assert.assertEquals("run " + run + " of " + DETERMINISM_RUNS, firstAlignments, alignments);
            }
        }
    }

    @Test
    public void givenChoiceIntoUnreachableJoin_whenCompute_thenNoOptimalAlignmentsAreReturned() throws AStarException {
        // set input
        AcceptingPetriNet apn = choiceIntoUnreachableJoinNet();
        // c can never fire (see choiceIntoUnreachableJoinNet), so the model's final marking is
        // unreachable regardless of the trace or how it's aligned
        XLog log = XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList("a", "b", "c"))).getOriginalLog();
        XTrace trace = log.get(0);

        // act
        PNMatchInstancesRepResult result = ALIGNMENTS.compute(apn, trace);

        // test
        Assert.assertTrue(result.iterator().next().getStepTypesLst().isEmpty());
    }
}
