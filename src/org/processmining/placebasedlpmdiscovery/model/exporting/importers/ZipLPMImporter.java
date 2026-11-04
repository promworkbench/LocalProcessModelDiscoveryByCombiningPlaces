package org.processmining.placebasedlpmdiscovery.model.exporting.importers;

import com.csvreader.CsvReader;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.ImportedEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Imports LPMs from a zip file as written by the
 * {@link org.processmining.placebasedlpmdiscovery.model.exporting.exporters.ZipLPMExporter}: one pnml file per LPM,
 * named {@code <prefix>.<lpmId>.pnml}, and optionally a csv file with the scores of the LPMs. Without the csv (or for
 * LPMs that have no row in it) the LPMs are imported without evaluation results.
 */
public class ZipLPMImporter {

    private static final String PNML_EXTENSION = ".pnml";
    private static final String CSV_EXTENSION = ".csv";
    // the event coverage written by the export is not imported
    private static final String COVERAGE_SUFFIX = "-coverage.csv";
    private static final String NAME_COLUMN = "Name";
    private static final Map<String, StandardLPMEvaluationResultId> SCORE_COLUMNS = new LinkedHashMap<>();

    static {
        SCORE_COLUMNS.put("Fitting Windows Score", StandardLPMEvaluationResultId.FittingWindowsEvaluationResult);
        SCORE_COLUMNS.put("Trace Support Score", StandardLPMEvaluationResultId.TraceSupportEvaluationResult);
        SCORE_COLUMNS.put("Lift Score", StandardLPMEvaluationResultId.LiftEvaluationResult);
    }

    private final List<String> warnings = new ArrayList<>();

    /**
     * @return the file name of the entry without the folders, or null if the entry should be ignored
     */
    private static String getFileName(ZipEntry entry) {
        if (entry.isDirectory() || entry.getName().startsWith("__MACOSX/"))
            return null;
        String name = entry.getName();
        String fileName = name.substring(Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\')) + 1);
        if (fileName.isEmpty() || fileName.startsWith("._"))
            return null;
        return fileName;
    }

    private static byte[] readEntry(ZipInputStream zip) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int read;
        while ((read = zip.read(buffer)) != -1)
            bytes.write(buffer, 0, read);
        return bytes.toByteArray();
    }

    /**
     * The export names the nets {@code <prefix>.<lpmId>.pnml}, where the prefix contains no dots.
     *
     * @return the lpm id, or null if the file name does not follow that pattern
     */
    static String extractLPMId(String fileName) {
        String withoutExtension = fileName.substring(0, fileName.length() - PNML_EXTENSION.length());
        int firstDot = withoutExtension.indexOf('.');
        if (firstDot < 0 || firstDot == withoutExtension.length() - 1)
            return null;
        return withoutExtension.substring(firstDot + 1);
    }

    /**
     * @param input       the zip content
     * @param zipFileName the name of the zip file, used to find the csv belonging to the export
     * @return the imported LPMs in the order in which they appear in the zip
     */
    public LPMResult read(InputStream input, String zipFileName) throws Exception {
        warnings.clear();
        Map<String, LocalProcessModel> lpms = new LinkedHashMap<>(); // pnml file name -> lpm
        Set<String> lpmIds = new HashSet<>();
        Map<String, byte[]> csvFiles = new LinkedHashMap<>(); // csv file name -> content

        ZipInputStream zip = new ZipInputStream(input);
        ZipEntry entry;
        while ((entry = zip.getNextEntry()) != null) {
            String fileName = getFileName(entry);
            if (fileName == null)
                continue;

            String lowerCaseName = fileName.toLowerCase(Locale.ROOT);
            if (lowerCaseName.endsWith(COVERAGE_SUFFIX)) {
                continue;
            } else if (lowerCaseName.endsWith(PNML_EXTENSION)) {
                if (lpms.containsKey(fileName)) {
                    warnings.add("Skipping " + entry.getName() + ": a net with the same file name was already " +
                            "imported.");
                    continue;
                }
                LocalProcessModel lpm = readLPM(readEntry(zip), fileName, lpmIds);
                lpmIds.add(lpm.getId());
                lpms.put(fileName, lpm);
            } else if (lowerCaseName.endsWith(CSV_EXTENSION)) {
                csvFiles.put(fileName, readEntry(zip));
            }
        }

        if (lpms.isEmpty())
            throw new IllegalArgumentException("The zip file " + zipFileName + " does not contain any pnml files.");

        byte[] csv = chooseCsv(csvFiles, zipFileName);
        if (csv != null)
            addScores(csv, lpms);
        else
            warnings.add("No csv file found; the LPMs are imported without scores.");

        LPMResult result = new LPMResult();
        result.addAll(lpms.values());
        return result;
    }

    /**
     * @return the problems that did not stop the last import, e.g. csv rows without a matching net
     */
    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    private LocalProcessModel readLPM(byte[] pnml, String fileName, Set<String> usedIds) throws Exception {
        AcceptingPetriNet net = PlaceUtils.extractAcceptingPetriNet(new ByteArrayInputStream(pnml), fileName);
        String id = extractLPMId(fileName);
        if (id != null && usedIds.contains(id)) {
            warnings.add("The id " + id + " of " + fileName + " was already used; a new id is generated.");
            id = null;
        }
        return id == null ?
                LocalProcessModelUtils.getLPMFromAcceptingPetriNetRepresentation(net) :
                LocalProcessModelUtils.getLPMFromAcceptingPetriNetRepresentation(net, id);
    }

    private byte[] chooseCsv(Map<String, byte[]> csvFiles, String zipFileName) {
        if (csvFiles.isEmpty())
            return null;
        int prefixEnd = zipFileName.indexOf('.');
        String expectedName = (prefixEnd < 0 ? zipFileName : zipFileName.substring(0, prefixEnd)) + CSV_EXTENSION;
        if (csvFiles.containsKey(expectedName))
            return csvFiles.get(expectedName);

        Map.Entry<String, byte[]> first = csvFiles.entrySet().iterator().next();
        if (csvFiles.size() > 1)
            warnings.add("Found " + csvFiles.size() + " csv files and none named " + expectedName + "; using "
                    + first.getKey() + ".");
        return first.getValue();
    }

    private void addScores(byte[] csv, Map<String, LocalProcessModel> lpms) throws IOException {
        CsvReader reader = new CsvReader(new InputStreamReader(new ByteArrayInputStream(csv), StandardCharsets.UTF_8)
                , ',');
        try {
            if (!reader.readHeaders() || reader.getIndex(NAME_COLUMN) < 0) {
                warnings.add("The csv file has no '" + NAME_COLUMN + "' column; the LPMs are imported without scores.");
                return;
            }

            Set<String> scoredLPMs = new HashSet<>();
            while (reader.readRecord()) {
                String name = reader.get(NAME_COLUMN).trim();
                String fileName = name.substring(Math.max(name.lastIndexOf('/'), name.lastIndexOf('\\')) + 1);
                LocalProcessModel lpm = lpms.get(fileName);
                if (lpm == null) {
                    warnings.add("The csv row for " + name + " has no matching pnml file.");
                    continue;
                }

                for (Map.Entry<String, StandardLPMEvaluationResultId> column : SCORE_COLUMNS.entrySet()) {
                    if (reader.getIndex(column.getKey()) < 0)
                        continue;
                    String value = reader.get(column.getKey()).trim();
                    if (value.isEmpty())
                        continue;
                    try {
                        double score = Double.parseDouble(value);
                        lpm.getAdditionalInfo().addCollectorResult(column.getValue().name(),
                                new ImportedEvaluationResult(column.getValue(), score));
                    } catch (NumberFormatException e) {
                        warnings.add("Invalid " + column.getKey() + " '" + value + "' for " + name + ".");
                    }
                }
                if (!lpm.getAdditionalInfo().getEvaluationResults().isEmpty())
                    scoredLPMs.add(fileName);
            }

            int unscored = lpms.size() - scoredLPMs.size();
            if (unscored > 0)
                warnings.add(unscored + " of " + lpms.size() + " LPMs have no valid scores in the csv and are " +
                        "imported without scores.");
        } finally {
            reader.close();
        }
    }
}
