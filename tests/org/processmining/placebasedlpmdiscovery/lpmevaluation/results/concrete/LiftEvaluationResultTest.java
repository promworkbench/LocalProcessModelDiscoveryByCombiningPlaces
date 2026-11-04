package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.junit.Assert;
import org.junit.Test;
import org.processmining.mockobjects.MockLPMs;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public class LiftEvaluationResultTest {

    private static final double DELTA = 1e-9;

    private static Map<String, Integer> counts(Object... activityCountPairs) {
        Map<String, Integer> res = new TreeMap<>();
        for (int i = 0; i < activityCountPairs.length; i += 2)
            res.put((String) activityCountPairs[i], (Integer) activityCountPairs[i + 1]);
        return res;
    }

    private static LiftEvaluationResult liftForAbc(int fitting, int total, Map<String, Integer> windowsContaining) {
        LiftEvaluationResult result = new LiftEvaluationResult(MockLPMs.getSequenceLPM_abc());
        result.updateCount(fitting);
        result.setTotal(total);
        result.setWindowsContainingActivity(windowsContaining);
        return result;
    }

    @Test
    public void givenSequenceLPM_whenCreated_thenActivitiesAreVisibleLabels() {
        LiftEvaluationResult result = new LiftEvaluationResult(MockLPMs.getSequenceLPM_abc());

        Assert.assertEquals(new HashSet<>(Arrays.asList("a", "b", "c")), result.getActivities());
    }

    @Test
    public void givenCounts_whenGetResult_thenFittingProbabilityDividedByProductOfActivityProbabilities() {
        // P(fit) = 5/100, P(a) = P(b) = P(c) = 20/100 -> lift = 0.05 / 0.008 = 6.25
        LiftEvaluationResult result = liftForAbc(5, 100, counts("a", 20, "b", 20, "c", 20, "x", 90));

        Assert.assertEquals(6.25, result.getResult(), DELTA);
        Assert.assertEquals(6.25 / 7.25, result.getNormalizedResult(), DELTA);
    }

    @Test
    public void givenLPMFittingAsOftenAsExpectedByChance_whenGetResult_thenOne() {
        // P(fit) = 1/8 = P(a) * P(b) * P(c) with each 1/2
        LiftEvaluationResult result = liftForAbc(100, 800, counts("a", 400, "b", 400, "c", 400));

        Assert.assertEquals(1.0, result.getResult(), DELTA);
        Assert.assertEquals(0.5, result.getNormalizedResult(), DELTA);
    }

    @Test
    public void givenRareActivitiesInLargeLog_whenGetResult_thenLargeLiftIsExact() {
        LiftEvaluationResult result = liftForAbc(1, 1_000_000_000,
                counts("a", 1, "b", 1, "c", 1));

        // lift = (1/N) / (1/N)^3 = N^2
        Assert.assertEquals(1e18, result.getResult(), 1e18 * 1e-9);
    }

    @Test
    public void givenNoTotals_whenGetResult_thenMinusOne() {
        LiftEvaluationResult result = new LiftEvaluationResult(MockLPMs.getSequenceLPM_abc());
        result.updateCount(5);

        Assert.assertEquals(-1, result.getResult(), DELTA);
        Assert.assertEquals(0, result.getNormalizedResult(), DELTA);
    }

    @Test
    public void givenNoFittingWindows_whenGetResult_thenMinusOne() {
        LiftEvaluationResult result = liftForAbc(0, 100, counts("a", 20, "b", 20, "c", 20));

        Assert.assertEquals(-1, result.getResult(), DELTA);
    }

    @Test
    public void givenActivityMissingFromCounts_whenGetResult_thenMinusOne() {
        LiftEvaluationResult result = liftForAbc(5, 100, counts("a", 20, "b", 20));

        Assert.assertEquals(-1, result.getResult(), DELTA);
    }

    @Test
    public void givenResult_whenClone_thenEqualAndIndependent() throws Exception {
        LiftEvaluationResult result = liftForAbc(5, 100, counts("a", 20, "b", 20, "c", 20));

        LiftEvaluationResult clone = result.clone();
        Assert.assertEquals(result, clone);

        clone.updateCount(1);
        Assert.assertNotEquals(result, clone);
        Assert.assertEquals(6.25, result.getResult(), DELTA);
    }
}
