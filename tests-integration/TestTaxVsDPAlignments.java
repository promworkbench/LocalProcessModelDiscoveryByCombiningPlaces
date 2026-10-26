import org.assertj.core.api.Assertions;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.junit.BeforeClass;
import org.junit.Test;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.PNAlignments;
import org.processmining.lpms.quality.alignments.dp.DPPNAlignments;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Computes the optimal alignments for every (lpm, trace) pair of a fixed lpm set / event log with
 * both {@link org.processmining.lpms.quality.alignments.tax.TaxPNAlignments} and
 * {@link DPPNAlignments}, and checks that the two algorithms agree on the set of optimal
 * alignments for every pair. Comparison happens pair-by-pair (rather than on the aggregated
 * per-log result) so a failure points directly at the offending (lpm, trace) pair.
 */
public class TestTaxVsDPAlignments {

    // TODO: point these at the actual lpm set / log files to use for this test.
    private static final String LPMS_FILE = "./data/test/lpms/tax/artificialBig";
    private static final String LOG_FILE = "./data/logs/artificialBig.xes";

    private static Map<String, AcceptingPetriNet> lpms;
    private static XLog log;

    @BeforeClass
    public static void loadInput() throws Exception {
        lpms = LpmAlignmentsFixtures.loadLpms(LPMS_FILE);
        log = LpmAlignmentsFixtures.loadLog(LOG_FILE);
    }

    @Test
    public void taxAndDpAlignments_debugAgreeOnTheSetOfOptimalAlignmentsForEveryLpmTracePair() throws Exception {
        PNAlignments tax = PNAlignments.tax();
        PNAlignments dp = new DPPNAlignments();

        int matchingPairs = 0;

        for (Map.Entry<String, AcceptingPetriNet> lpmEntry : lpms.entrySet()) {
            String lpm = lpmEntry.getKey();
            AcceptingPetriNet apn = lpmEntry.getValue();
//            System.out.println("Testing lpm " + lpm + " against all traces in the log");

            int traceIndex = 0;
            for (XTrace trace : log) {
                String traceString = trace.stream().map(e -> e.getAttributes().get("concept:name").toString())
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");
//                System.out.println(traceString);

                // act
                List<String> taxAlignments = LpmAlignmentsFixtures.computeOptimalAlignments(apn, trace, tax);
                List<String> dpAlignments = LpmAlignmentsFixtures.computeOptimalAlignments(apn, trace, dp);

                if (dpAlignments.equals(taxAlignments)) {
                    matchingPairs++;
                } else {
                    List<String> onlyInDp = new ArrayList<>(dpAlignments);
                    onlyInDp.removeAll(taxAlignments);
                    List<String> onlyInTax = new ArrayList<>(taxAlignments);
                    onlyInTax.removeAll(dpAlignments);

//                    System.out.printf("lpm %s, trace %s: DPPNAlignments should compute the same set of " +
//                            "optimal alignments as TaxPNAlignments%n", lpm, traceIndex);

                    if (!onlyInTax.isEmpty()) {
                        throw new IllegalStateException("some");
                    }

                    System.out.println("Trace: " + traceIndex + " lpm " + lpm);
                    onlyInDp.removeAll(taxAlignments);
                    onlyInDp = onlyInDp.stream().map(s -> {
                        String[] parts = s.split("-");
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < parts.length; i++) {
                            if (parts[i].equals("L")) {
                                continue;
                            }
                            sb.append(trace.get(i).getAttributes().get("concept:name").toString());
                        }
                        return sb.toString();
                    }).collect(Collectors.toCollection(ArrayList::new));

//                    System.out.println("lpm " + lpm + ", trace " + traceIndex);
                    System.out.println("Only in DPPNAlignments: " + new HashSet<>(onlyInDp));

//                    System.out.println(traceIndex);
//                    System.out.printf("lpm %s, trace %s: DPPNAlignments should compute the same set of " +
//                            "optimal alignments as TaxPNAlignments%n", lpm, traceString);
//                    System.out.println("Only in DPPNAlignments: " + onlyInDp);
//                    System.out.println("Only in TaxPNAlignments: " + onlyInTax);
                }

//                // assert: same set of optimal alignments for this exact (lpm, trace) pair
//                Assertions.assertThat(dpAlignments)
//                        .as("lpm %s, trace %s: DPPNAlignments should compute the same set of optimal "
//                                        + "alignments as TaxPNAlignments",
//                                lpm, traceString)
//                        .isEqualTo(taxAlignments);

                traceIndex++;
            }
        }
        System.out.printf("Tested %d (lpm, trace) pairs); %d pairs had matching optimal alignments between " +
                        "TaxPNAlignments and DPPNAlignments%n",
                lpms.size() * log.size(), matchingPairs);
    }

    @Test
    public void taxAndDpAlignments_agreeOnTheSetOfOptimalAlignmentsForEveryLpmTracePair() throws Exception {
        PNAlignments tax = PNAlignments.tax();
        PNAlignments dp = new DPPNAlignments();

        for (Map.Entry<String, AcceptingPetriNet> lpmEntry : lpms.entrySet()) {
            String lpm = lpmEntry.getKey();
            AcceptingPetriNet apn = lpmEntry.getValue();

            for (XTrace trace : log) {
                String traceString = trace.stream().map(e -> e.getAttributes().get("concept:name").toString())
                        .reduce((a, b) -> a + "," + b)
                        .orElse("");

                // act
                List<String> taxAlignments = LpmAlignmentsFixtures.computeOptimalAlignments(apn, trace, tax);
                List<String> dpAlignments = LpmAlignmentsFixtures.computeOptimalAlignments(apn, trace, dp);

                // assert: same set of optimal alignments for this exact (lpm, trace) pair
                Assertions.assertThat(dpAlignments)
                        .as("lpm %s, trace %s: DPPNAlignments should compute the same set of optimal "
                                        + "alignments as TaxPNAlignments",
                                lpm, traceString)
                        .isEqualTo(taxAlignments);
            }
        }
    }

    @Test
    public void givenLpmAndTraceIndex_whenCompute_thenDifferenceBetweenDpAndTaxAlignmentsIsComputed() throws Exception {
        // TODO: point these at the lpm key (as loaded into `lpms`, e.g. a .pnml file name) and trace index to debug.
        String lpmKey = "tax_artificialBig_62.pnml";
        int traceIndex = 25;

        AcceptingPetriNet apn = lpms.get(lpmKey);
        XTrace trace = log.get(traceIndex);

        PNAlignments tax = PNAlignments.tax();
        PNAlignments dp = new DPPNAlignments();

        List<String> taxAlignments = LpmAlignmentsFixtures.computeOptimalAlignments(apn, trace, tax);
        List<String> dpAlignments = LpmAlignmentsFixtures.computeOptimalAlignments(apn, trace, dp);

        System.out.println("Tax Alignments: " + taxAlignments.size());
        System.out.println("DP Alignments: " + dpAlignments.size());

        List<String> onlyInDp = new ArrayList<>(dpAlignments);
        onlyInDp.removeAll(taxAlignments);
//        Set<String> onlyInDpSet = new HashSet<>(onlyInDp);
//        if (onlyInDp.size() == onlyInDpSet.size()) {
//            System.out.println("No duplicates in DPPNAlignments");
//        } else {
//            System.out.println("Duplicates in DPPNAlignments");
//        }
//        onlyInDpSet.removeAll(new HashSet<>(taxAlignments));
        onlyInDp = onlyInDp.stream().map(s -> {
            String[] parts = s.split("-");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < parts.length; i++) {
                if (parts[i].equals("L")) {
                    continue;
                }
                sb.append(trace.get(i).getAttributes().get("concept:name").toString());
            }
            return sb.toString();
        }).collect(Collectors.toCollection(ArrayList::new));

        List<String> onlyInTax = new ArrayList<>(taxAlignments);
        onlyInTax.removeAll(dpAlignments);

//        taxAlignments = taxAlignments.stream().map(s -> {
//            String[] parts = s.split("-");
//            StringBuilder sb = new StringBuilder();
//            for (int i = 0; i < parts.length; i++) {
//                if (parts[i].equals("L")) {
//                    continue;
//                }
//                sb.append(trace.get(i).getAttributes().get("concept:name").toString());
//            }
//            return sb.toString();
//        }).collect(Collectors.toCollection(ArrayList::new));

        String traceString = trace.stream().map(e -> e.getAttributes().get("concept:name").toString())
                .reduce((a, b) -> a + "," + b)
                .orElse("");
        System.out.println(traceString);

        System.out.println("lpm " + lpmKey + ", trace " + traceIndex);
        System.out.println("Only in DPPNAlignments: " + onlyInDp);
        System.out.println("Only in TaxPNAlignments: " + onlyInTax);
    }
}
