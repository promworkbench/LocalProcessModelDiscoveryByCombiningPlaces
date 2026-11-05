import org.assertj.core.api.Assertions;
import org.deckfour.xes.model.XLog;
import org.junit.BeforeClass;
import org.junit.Test;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Computes all optimal alignments for every (lpm, trace) pair of a fixed lpm set / event log and
 * checks the result against a checked-in expected fixture, run repeatedly within the same JVM.
 * A mismatch means either a real regression or that the alignment computation is not
 * deterministic across runs - both are failures worth catching here.
 */
public class TestLPMAlignmentsDeterminism {

    // TODO: point these at the actual lpm set / log / expected-fixture files to use for this test.
    private static final String LPMS_FILE = "./data/lpms/artificialBig.json";
    private static final String LOG_FILE = "./data/logs/artificialBig.xes";
    private static final String EXPECTED_ALIGNMENTS_FILE = "./data/test/alignments/artificialBig_expected.json";

    // Where the freshly computed result is (re-)written on every run, so it can be inspected or
    // used to regenerate EXPECTED_ALIGNMENTS_FILE when the fixture is intentionally updated.
    private static final String ACTUAL_ALIGNMENTS_FILE = "./data/test/alignments/TODO_SET_EXPECTED_FILE.actual.json";

    private static final int RUNS = 5;

    private static Map<String, AcceptingPetriNet> lpms;
    private static XLog log;

    @BeforeClass
    public static void loadInput() throws Exception {
        lpms = LpmAlignmentsFixtures.loadLpms(LPMS_FILE);
        log = LpmAlignmentsFixtures.loadLog(LOG_FILE);
    }

    @Test
    public void computingAllOptimalAlignments_isDeterministicAndMatchesExpectedFixture() throws Exception {
        // arrange
        TreeMap<String, TreeMap<String, List<String>>> expected =
                LpmAlignmentsFixtures.readAlignments(EXPECTED_ALIGNMENTS_FILE);

        for (int run = 0; run < RUNS; run++) {
            // act
            TreeMap<String, TreeMap<String, List<String>>> actual =
                    LpmAlignmentsFixtures.computeAllOptimalAlignments(lpms, log);
            LpmAlignmentsFixtures.writeAlignments(actual, ACTUAL_ALIGNMENTS_FILE);

            // assert: same set of optimal alignments per lpm/trace pair on every run
            Assertions.assertThat(actual)
                    .as("run %d of %d should match the checked-in expected alignments (%s)",
                            run + 1, RUNS, EXPECTED_ALIGNMENTS_FILE)
                    .isEqualTo(expected);
        }
    }
}