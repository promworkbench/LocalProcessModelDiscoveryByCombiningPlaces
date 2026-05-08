package org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer;

import org.junit.Test;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;

import java.util.Arrays;
import java.util.Collections;

public class LEFRMatrixTest {

    private static XLogWrapper singleTrace(String... events) {
        return XLogWrapper.fromListOfTracesAsListStrings(
                Collections.singletonList(Arrays.asList(events)));
    }

    @Test
    public void givenSequentialTrace_withDirectFollowLimit_whenGet_thenOnlyDirectFollowsCounted() {
        LEFRMatrix matrix = new LEFRMatrix(singleTrace("a", "b", "c").getOriginalLog(), 2);

        assert matrix.get("a", "b") == 1;
        assert matrix.get("b", "c") == 1;
        assert matrix.get("a", "c") == 0;
    }

    @Test
    public void givenSequentialTrace_withWiderLimit_whenGet_thenAllPairsWithinWindowCounted() {
        LEFRMatrix matrix = new LEFRMatrix(singleTrace("a", "b", "c").getOriginalLog(), 3);

        assert matrix.get("a", "b") == 1;
        assert matrix.get("b", "c") == 1;
        assert matrix.get("a", "c") == 1;
    }

    @Test
    public void givenMultipleTraces_whenGet_thenCountsAreAggregatedAcrossTraces() {
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList("a", "b")));
        LEFRMatrix matrix = new LEFRMatrix(log.getOriginalLog(), 2);

        assert matrix.get("a", "b") == 2;
    }

    @Test
    public void givenSingleEventTrace_whenGet_thenAllCountsAreZero() {
        LEFRMatrix matrix = new LEFRMatrix(singleTrace("a").getOriginalLog(), 2);

        assert matrix.get("a", "a") == 0;
    }

    @Test
    public void givenActivityNotInLog_withDirectFollowLimit_whenGet_thenReturnsZero() {
        LEFRMatrix matrix = new LEFRMatrix(singleTrace("a", "b").getOriginalLog(), 2);

        assert matrix.get("x", "y") == 0;
    }

    @Test
    public void givenActivityNotInLog_withWiderLimit_whenGet_thenReturnsZero() {
        LEFRMatrix matrix = new LEFRMatrix(singleTrace("a", "b", "c").getOriginalLog(), 3);

        assert matrix.get("x", "y") == 0;
    }

    @Test
    public void givenRepeatedActivity_whenGet_thenSelfFollowCounted() {
        LEFRMatrix matrix = new LEFRMatrix(singleTrace("a", "a", "b").getOriginalLog(), 2);

        assert matrix.get("a", "a") == 1;
        assert matrix.get("a", "b") == 1;
    }

    @Test
    public void givenTraceWithConsecutiveRepeatedActivities_withWiderLimit_whenGet_thenCountsAreCorrect() {
        LEFRMatrix matrix = new LEFRMatrix(singleTrace("a", "a", "b", "b", "c").getOriginalLog(), 4);

        assert matrix.get("a", "a") == 1;
        assert matrix.get("a", "b") == 4;
        assert matrix.get("a", "c") == 1;
        assert matrix.get("b", "b") == 1;
        assert matrix.get("b", "c") == 2;
        assert matrix.get("b", "a") == 0;
        assert matrix.get("c", "a") == 0;
    }

    @Test
    public void givenMultipleTracesWithDifferentPairs_whenGet_thenEachPairCountedIndependently() {
        XLogWrapper log = XLogWrapper.fromListOfTracesAsListStrings(Arrays.asList(
                Arrays.asList("a", "b"),
                Arrays.asList("a", "c")));
        LEFRMatrix matrix = new LEFRMatrix(log.getOriginalLog(), 2);

        assert matrix.get("a", "b") == 1;
        assert matrix.get("a", "c") == 1;
        assert matrix.get("b", "c") == 0;
    }
}
