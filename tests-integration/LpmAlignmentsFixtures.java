import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nl.tue.astar.AStarException;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.lpms.quality.alignments.PNAlignments;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.results.FromFileLPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;
import org.processmining.plugins.petrinet.replayresult.PNMatchInstancesRepResult;
import org.processmining.plugins.petrinet.replayresult.StepTypes;
import org.processmining.plugins.replayer.replayresult.AllSyncReplayResult;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Shared helpers for the LPM/alignments integration tests: loading LPMs and logs from data files,
 * computing all optimal alignments per (lpm, trace) pair via {@link PNAlignments}, and
 * serializing/deserializing that result to/from a canonical JSON fixture file.
 */
final class LpmAlignmentsFixtures {

    /**
     * lpm key -> trace index (zero-padded) -> sorted, deduped list of optimal
     * alignments, each alignment serialized as its step types joined with "-".
     */
    static final Type ALIGNMENTS_TYPE =
            new TypeToken<TreeMap<String, TreeMap<String, List<String>>>>() {
            }.getType();

    private LpmAlignmentsFixtures() {
    }

    /**
     * Loads an lpm set as accepting Petri nets, sorted by key. Two input variants are supported:
     * <ul>
     *     <li>a <b>directory</b> of {@code .pnml} files: one entry per file, keyed by file name;</li>
     *     <li>a <b>file</b> readable by {@link FromFileLPMDiscoveryResult} (e.g. an LPM {@code .json}):
     *     one entry per LPM, keyed by {@link LocalProcessModel#getShortString()}.</li>
     * </ul>
     */
    static Map<String, AcceptingPetriNet> loadLpms(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        if (Files.isDirectory(path)) {
            return loadLpmsFromPnmlDirectory(path);
        }
        if (Files.isRegularFile(path)) {
            return loadLpmsFromLpmFile(filePath);
        }
        throw new IOException("LPM source does not exist: " + path.toAbsolutePath());
    }

    private static Map<String, AcceptingPetriNet> loadLpmsFromPnmlDirectory(Path directory) throws IOException {
        try (Stream<Path> paths = Files.list(directory)) {
            List<Path> pnmlFiles = paths
                    .filter(path -> path.toString().endsWith(".pnml"))
                    .sorted()
                    .collect(Collectors.toList());

            Map<String, AcceptingPetriNet> lpms = new TreeMap<>();
            for (Path pnmlFile : pnmlFiles) {
                lpms.put(pnmlFile.getFileName().toString(), PlaceUtils.extractAcceptingPetriNet(pnmlFile.toString()));
            }
            return lpms;
        } catch (XmlPullParserException e) {
            throw new IOException("Failed to parse a PNML file in " + directory, e);
        }
    }

    private static Map<String, AcceptingPetriNet> loadLpmsFromLpmFile(String filePath) throws IOException {
        Map<String, AcceptingPetriNet> lpms = new TreeMap<>();
        for (LocalProcessModel lpm : new FromFileLPMDiscoveryResult(filePath).getAllLPMs()) {
            String key = lpm.getShortString();
            if (lpms.put(key, LocalProcessModelUtils.getAcceptingPetriNetRepresentation(lpm)) != null) {
                throw new IOException("Duplicate LPM " + key + " in " + filePath);
            }
        }
        return lpms;
    }

    static XLog loadLog(String filePath) throws Exception {
        return LogUtils.readLogFromFile(filePath);
    }

    /**
     * Computes, for every (lpm, trace) pair, the set of all tied-optimal alignments, keyed
     * deterministically so the result can be compared byte-for-byte across runs.
     */
    static TreeMap<String, TreeMap<String, List<String>>> computeAllOptimalAlignments(
            Map<String, AcceptingPetriNet> lpms, XLog log) throws AStarException {
        return computeAllOptimalAlignments(lpms, log, PNAlignments.tax());
    }

    /**
     * Same as {@link #computeAllOptimalAlignments(Map, XLog)} but using {@code algorithm} instead of
     * the default {@link PNAlignments#tax()}.
     */
    static TreeMap<String, TreeMap<String, List<String>>> computeAllOptimalAlignments(
            Map<String, AcceptingPetriNet> lpms, XLog log, PNAlignments algorithm) throws AStarException {
        TreeMap<String, TreeMap<String, List<String>>> byLpm = new TreeMap<>();

        for (Map.Entry<String, AcceptingPetriNet> lpm : lpms.entrySet()) {
            PNMatchInstancesRepResult result = algorithm.compute(lpm.getValue(), log);

            TreeMap<String, List<String>> byTrace = new TreeMap<>();
            for (AllSyncReplayResult r : result) {
                List<String> alignments = r.getStepTypesLst().stream()
                        .map(LpmAlignmentsFixtures::serializeAlignment)
                        .sorted()
                        .distinct()
                        .collect(Collectors.toList());
                for (int traceIndex : r.getTraceIndex()) {
                    byTrace.put(traceKey(traceIndex), alignments);
                }
            }
            byLpm.put(lpm.getKey(), byTrace);
        }

        return byLpm;
    }

    /**
     * Computes the set of all tied-optimal alignments for a single (lpm, trace) pair using
     * {@code algorithm}, sorted and deduped the same way as {@link #computeAllOptimalAlignments} so
     * results from different algorithms can be compared pair-by-pair.
     */
    static List<String> computeOptimalAlignments(AcceptingPetriNet apn, XTrace trace, PNAlignments algorithm)
            throws AStarException {
        PNMatchInstancesRepResult result = algorithm.compute(apn, trace);
        return result.stream()
                .flatMap(r -> r.getStepTypesLst().stream())
                .map(LpmAlignmentsFixtures::serializeAlignment)
                .sorted()
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * Computes all optimal alignments for the given lpm set / log and writes them to
     * {@code expectedAlignmentsFilePath}, overwriting whatever fixture is checked in there. Intended
     * to be run manually to create or intentionally update an expected-alignments fixture, never as
     * part of the regular test suite. {@code lpmsFilePath} may be either variant accepted by
     * {@link #loadLpms(String)}.
     */
    static void generateExpectedAlignments(String lpmsFilePath, String logFilePath, String expectedAlignmentsFilePath)
            throws Exception {
        Map<String, AcceptingPetriNet> lpms = loadLpms(lpmsFilePath);
        XLog log = loadLog(logFilePath);
        TreeMap<String, TreeMap<String, List<String>>> alignments = computeAllOptimalAlignments(lpms, log);
        writeAlignments(alignments, expectedAlignmentsFilePath);
    }

    static String traceKey(int traceIndex) {
        return String.format("%06d", traceIndex);
    }

    static String serializeAlignment(List<StepTypes> steps) {
        return steps.stream().map(Enum::name).collect(Collectors.joining("-"));
    }

    static void writeAlignments(TreeMap<String, TreeMap<String, List<String>>> alignments, String filePath)
            throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Files.createDirectories(Paths.get(filePath).toAbsolutePath().getParent());
        try (Writer writer = Files.newBufferedWriter(Paths.get(filePath))) {
            gson.toJson(alignments, ALIGNMENTS_TYPE, writer);
        }
    }

    static TreeMap<String, TreeMap<String, List<String>>> readAlignments(String filePath) throws IOException {
        Gson gson = new Gson();
        try (Reader reader = Files.newBufferedReader(Paths.get(filePath))) {
            return gson.fromJson(reader, ALIGNMENTS_TYPE);
        }
    }
}
