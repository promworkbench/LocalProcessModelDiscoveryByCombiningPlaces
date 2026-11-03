package org.processmining.placebasedlpmdiscovery.model.exporting.exporters;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;
import org.processmining.mockobjects.MockLPMs;
import org.processmining.mockobjects.MockPlaces;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.ImportedEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.exporting.importers.ZipLPMImporter;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipLPMExporterTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static LocalProcessModel withScores(LocalProcessModel lpm, double fittingWindows, double traceSupport) {
        lpm.getAdditionalInfo().addCollectorResult(StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.name(),
                new ImportedEvaluationResult(StandardLPMEvaluationResultId.FittingWindowsEvaluationResult,
                        fittingWindows));
        lpm.getAdditionalInfo().addCollectorResult(StandardLPMEvaluationResultId.TraceSupportEvaluationResult.name(),
                new ImportedEvaluationResult(StandardLPMEvaluationResultId.TraceSupportEvaluationResult, traceSupport));
        return lpm;
    }

    private static LPMTemporaryWindowInfo window(int traceVariantId, int windowCount, Integer... replayedEvents) {
        LPMTemporaryWindowInfo info = Mockito.mock(LPMTemporaryWindowInfo.class);
        Mockito.when(info.getTraceVariantId()).thenReturn(traceVariantId);
        Mockito.when(info.getWindowCount()).thenReturn(windowCount);
        Mockito.when(info.getReplayedEventsIndices()).thenReturn(Arrays.asList(replayedEvents));
        return info;
    }

    private static Map<String, String> readZip(File zip) throws IOException {
        Map<String, String> entries = new LinkedHashMap<>();
        try (ZipInputStream in = new ZipInputStream(Files.newInputStream(zip.toPath()))) {
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1)
                    bytes.write(buffer, 0, read);
                entries.put(entry.getName(), new String(bytes.toByteArray(), StandardCharsets.UTF_8));
            }
        }
        return entries;
    }

    private static List<String> lines(String content) {
        return Arrays.asList(content.trim().split("\r?\n"));
    }

    @Test
    public void givenResultWithoutCoverage_whenExport_thenNetsAndScoresCsvAreWritten() throws Exception {
        // given
        LocalProcessModel lpm = withScores(MockLPMs.getSequenceLPM_abc(), 0.25, 0.75);
        LPMResult result = new LPMResult();
        result.add(lpm);
        File zip = new File(tmp.getRoot(), "lpms.zip");

        // when
        ZipLPMExporter.export(result, zip, null);

        // then
        Map<String, String> entries = readZip(zip);
        String pnmlName = "lpms." + lpm.getId() + ".pnml";
        Assert.assertEquals(Arrays.asList(pnmlName, "lpms.csv"), new ArrayList<>(entries.keySet()));
        List<String> csv = lines(entries.get("lpms.csv"));
        Assert.assertEquals("Name,Short String,Fitting Windows Score,Trace Support Score,Aggregated Score",
                csv.get(0));
        Assert.assertTrue(csv.get(1), csv.get(1).startsWith(pnmlName + ","));
        Assert.assertTrue(csv.get(1), csv.get(1).contains(",0.25,0.75,"));
    }

    @Test
    public void givenResultWithCoverage_whenExport_thenCoverageRowsAreNamedByPnmlFile() throws Exception {
        // given
        LocalProcessModel covering = withScores(MockLPMs.getSequenceLPM_abc(), 0.25, 0.75);
        LocalProcessModel uncovered = withScores(new LocalProcessModel(MockPlaces.getChoiceInPlace()), 0.5, 0.5);
        LocalProcessModel notExported = new LocalProcessModel(MockPlaces.getDoubleChoicePlace());
        LPMEvaluationController.EventCoverageSetLevel coverage =
                new LPMEvaluationController().getEventCoverageSetLevel();
        coverage.update(covering, window(7, 3, 0, 2));
        coverage.update(notExported, window(9, 5, 1));

        LPMResult result = new LPMResult();
        result.add(covering);
        result.add(uncovered);
        result.addAdditionalResults(ZipLPMExporter.EVENT_COVERAGE_KEY, coverage);
        File zip = new File(tmp.getRoot(), "lpms.zip");

        // when
        ZipLPMExporter.export(result, zip, null);

        // then
        Map<String, String> entries = readZip(zip);
        Assert.assertTrue(entries.keySet().toString(), entries.containsKey("lpms-coverage.csv"));
        Assert.assertEquals(Arrays.asList(
                "LPM,7_0,7_2,9_1",
                "lpms." + covering.getId() + ".pnml,3,3,0",
                "lpms." + uncovered.getId() + ".pnml,0,0,0"), lines(entries.get("lpms-coverage.csv")));

        // the coverage csv is not mistaken for the scores csv on import
        ZipLPMImporter importer = new ZipLPMImporter();
        LPMResult imported = importer.read(Files.newInputStream(zip.toPath()), zip.getName());
        Assert.assertEquals(2, imported.size());
        Assert.assertTrue(importer.getWarnings().toString(), importer.getWarnings().isEmpty());
    }
}
