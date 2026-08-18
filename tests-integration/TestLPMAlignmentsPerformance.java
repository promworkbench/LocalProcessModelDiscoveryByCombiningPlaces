import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.PNAlignments;

import java.util.Map;

/**
 * Measures how long it takes to compute all optimal alignments for a fixed lpm set / event log,
 * two ways:
 * 1. per (lpm, trace) pair - each pair timed individually, averaged over every pair.
 * 2. per lpm over the whole log at once - timed as a single batch call per lpm, repeated
 * WHOLE_LOG_RUNS times and averaged.
 * There's no fixed pass/fail threshold since wall-clock time is machine dependent; the test
 * prints both averages and only fails if the computation itself throws or produces no samples.
 */
public class TestLPMAlignmentsPerformance {

    // TODO: point these at the actual lpm set / log files to use for this benchmark.
    private static final String LPMS_FILE = "./data/test/lpms/tax/artificialBig";
    private static final String LOG_FILE = "./data/logs/artificialBig.xes";

    private static final int WHOLE_LOG_RUNS = 10;

    private static Map<String, AcceptingPetriNet> lpms;
    private static XLog log;

    @BeforeClass
    public static void loadInput() throws Exception {
        lpms = LpmAlignmentsFixtures.loadLpms(LPMS_FILE);
        log = LpmAlignmentsFixtures.loadLog(LOG_FILE);
    }

    @Test
    public void measureAveragePerPairAlignmentTime() throws Exception {
        long totalNanos = 0;
        long pairCount = 0;

        for (String lpm : lpms.keySet()) {
            AcceptingPetriNet apn = lpms.get(lpm);
            for (XTrace trace : log) {
                long start = System.nanoTime();
                PNAlignments.tax().compute(apn, trace);
                totalNanos += System.nanoTime() - start;
                pairCount++;
            }
        }

        Assert.assertTrue("expected at least one (lpm, trace) pair to time", pairCount > 0);
        double avgMillisPerPair = totalNanos / 1_000_000.0 / pairCount;

        System.out.printf(
                "[TestLPMAlignmentsPerformance] avg time per (lpm, trace) pair: %.3f ms over %d pairs (%d lpms x %d " +
                        "traces)%n",
                avgMillisPerPair, pairCount, lpms.size(), log.size());
    }

    @Test
    public void measureAverageWholeLogAlignmentTime() throws Exception {
        long totalNanos = 0;
        int samples = 0;

        for (int run = 0; run < WHOLE_LOG_RUNS; run++) {
            for (String lpm : lpms.keySet()) {
                AcceptingPetriNet apn = lpms.get(lpm);
                long start = System.nanoTime();
                PNAlignments.tax().compute(apn, log);
                totalNanos += System.nanoTime() - start;
                samples++;
            }
        }

        Assert.assertTrue("expected at least one whole-log alignment sample", samples > 0);
        double avgMillisPerRun = totalNanos / 1_000_000.0 / samples;

        System.out.printf(
                "[TestLPMAlignmentsPerformance] avg time to align a whole log (%d traces) per lpm: %.3f ms, "
                        + "averaged over %d runs x %d lpms%n",
                log.size(), avgMillisPerRun, WHOLE_LOG_RUNS, lpms.size());
    }
}