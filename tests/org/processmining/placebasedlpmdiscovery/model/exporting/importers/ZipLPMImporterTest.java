package org.processmining.placebasedlpmdiscovery.model.exporting.importers;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetImpl;
import org.processmining.mockobjects.MockLPMs;
import org.processmining.mockobjects.MockPlaces;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.graphbased.directed.petrinet.elements.Place;
import org.processmining.models.graphbased.directed.petrinet.elements.Transition;
import org.processmining.models.graphbased.directed.petrinet.impl.PetrinetFactory;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.ImportedEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.undecided.Utils;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.exporting.exporters.ExporterFactory;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.utils.LocalProcessModelUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipLPMImporterTest {

    private static final String FITTING_WINDOWS = StandardLPMEvaluationResultId.FittingWindowsEvaluationResult.name();
    private static final String TRACE_SUPPORT = StandardLPMEvaluationResultId.TraceSupportEvaluationResult.name();

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private static LocalProcessModel withScores(LocalProcessModel lpm, double fittingWindows, double traceSupport) {
        lpm.getAdditionalInfo().addCollectorResult(FITTING_WINDOWS, new ImportedEvaluationResult(
                StandardLPMEvaluationResultId.FittingWindowsEvaluationResult, fittingWindows));
        lpm.getAdditionalInfo().addCollectorResult(TRACE_SUPPORT, new ImportedEvaluationResult(
                StandardLPMEvaluationResultId.TraceSupportEvaluationResult, traceSupport));
        return lpm;
    }

    private static double score(LocalProcessModel lpm, String key) {
        return lpm.getAdditionalInfo().getEvaluationResult(key, LPMEvaluationResult.class).getResult();
    }

    private static List<String> tokens(LocalProcessModel lpm) {
        return lpm.getPlaces().stream()
                .map(p -> p.getShortString() + "=" + p.getNumTokens())
                .sorted()
                .collect(Collectors.toList());
    }

    private static Map<String, LocalProcessModel> byId(LPMResult result) {
        return result.getElements().stream().collect(Collectors.toMap(LocalProcessModel::getId, Function.identity()));
    }

    @Test
    public void givenExportedZipWithCsv_whenRead_thenStructureIdsAndScoresAreRestored() throws Exception {
        // given
        LocalProcessModel sequence = withScores(MockLPMs.getSequenceLPM_abc(), 0.25, 0.75);
        LocalProcessModel choice = withScores(new LocalProcessModel(MockPlaces.getChoiceOutPlace()), 0.5, 0.125);
        choice.getPlaces().iterator().next().setNumTokens(1);
        File zip = export("lpms.zip", sequence, choice);

        // when
        ZipLPMImporter importer = new ZipLPMImporter();
        LPMResult result = importer.read(Files.newInputStream(zip.toPath()), zip.getName());

        // then
        Assert.assertEquals(2, result.size());
        Map<String, LocalProcessModel> imported = byId(result);
        for (LocalProcessModel original : Arrays.asList(sequence, choice)) {
            LocalProcessModel lpm = imported.get(original.getId());
            Assert.assertNotNull("LPM id " + original.getId() + " should be restored", lpm);
            Assert.assertEquals(original.getShortString(), lpm.getShortString());
            Assert.assertEquals(original.getArcs().size(), lpm.getArcs().size());
            Assert.assertEquals(tokens(original), tokens(lpm));
            Assert.assertEquals(score(original, FITTING_WINDOWS), score(lpm, FITTING_WINDOWS), 1e-9);
            Assert.assertEquals(score(original, TRACE_SUPPORT), score(lpm, TRACE_SUPPORT), 1e-9);
        }
        Assert.assertTrue(importer.getWarnings().isEmpty());
    }

    @Test
    public void givenZipWithoutCsv_whenRead_thenLPMsHaveNoScoresAndCanBeExportedAgain() throws Exception {
        // given
        LocalProcessModel lpm = withScores(MockLPMs.getSequenceLPM_abc(), 0.25, 0.75);
        File zip = replaceCsv(export("lpms.zip", lpm), null);

        // when
        ZipLPMImporter importer = new ZipLPMImporter();
        LPMResult result = importer.read(Files.newInputStream(zip.toPath()), zip.getName());

        // then
        Assert.assertEquals(1, result.size());
        LocalProcessModel imported = result.getElement(0);
        Assert.assertEquals(lpm.getId(), imported.getId());
        Assert.assertEquals(lpm.getShortString(), imported.getShortString());
        Assert.assertTrue(imported.getAdditionalInfo().getEvaluationResults().isEmpty());
        Assert.assertEquals(1, importer.getWarnings().size());

        // re-exporting LPMs without scores must not fail
        LocalProcessModelUtils.exportResult(result, tmp.newFile("reexport.zip"));
    }

    @Test
    public void givenCsvWithReorderedColumnsMissingRowsAndInvalidValues_whenRead_thenAvailableScoresAreUsed()
            throws Exception {
        // given
        LocalProcessModel withRow = MockLPMs.getSequenceLPM_abc();
        LocalProcessModel withoutRow = new LocalProcessModel(MockPlaces.getChoiceInPlace());
        LocalProcessModel withInvalidValue = new LocalProcessModel(MockPlaces.getDoubleChoicePlace());
        File zip = export("lpms.zip", withScores(withRow, 0, 0), withScores(withoutRow, 0, 0),
                withScores(withInvalidValue, 0, 0));
        String csv = "Trace Support Score,Name\n" +
                "0.4,lpms." + withRow.getId() + ".pnml\n" +
                "abc,lpms." + withInvalidValue.getId() + ".pnml\n" +
                "0.9,lpms.does-not-exist.pnml\n";
        zip = replaceCsv(zip, csv);

        // when
        ZipLPMImporter importer = new ZipLPMImporter();
        Map<String, LocalProcessModel> imported = byId(importer.read(Files.newInputStream(zip.toPath()),
                zip.getName()));

        // then
        LocalProcessModel lpm = imported.get(withRow.getId());
        Assert.assertEquals(0.4, score(lpm, TRACE_SUPPORT), 1e-9);
        Assert.assertNull(lpm.getAdditionalInfo().getEvaluationResult(FITTING_WINDOWS, LPMEvaluationResult.class));
        Assert.assertTrue(imported.get(withoutRow.getId()).getAdditionalInfo().getEvaluationResults().isEmpty());
        Assert.assertTrue(imported.get(withInvalidValue.getId()).getAdditionalInfo().getEvaluationResults().isEmpty());
        // invalid value, csv row without net, summary of LPMs without scores
        Assert.assertEquals(importer.getWarnings().toString(), 3, importer.getWarnings().size());
        // the LPM whose only value is invalid counts as unscored, like the one without a row
        Assert.assertTrue(importer.getWarnings().toString(), importer.getWarnings().get(2).startsWith("2 of 3 LPMs"));
    }

    @Test
    public void givenNetWithTwoInvisibleTransitionsWithSameLabel_whenRead_thenBothTransitionsAreKept() throws Exception {
        // given: a -> p1 -> tau -> p2 -> tau -> p3 -> b
        Petrinet net = PetrinetFactory.newPetrinet("net");
        Transition a = net.addTransition("a");
        Transition tau1 = net.addTransition("tau");
        tau1.setInvisible(true);
        Transition tau2 = net.addTransition("tau");
        tau2.setInvisible(true);
        Transition b = net.addTransition("b");
        Place p1 = net.addPlace("p1");
        Place p2 = net.addPlace("p2");
        Place p3 = net.addPlace("p3");
        net.addArc(a, p1);
        net.addArc(p1, tau1);
        net.addArc(tau1, p2);
        net.addArc(p2, tau2);
        net.addArc(tau2, p3);
        net.addArc(p3, b);
        ByteArrayOutputStream pnml = new ByteArrayOutputStream();
        Utils.exportAcceptingPetriNetToOutputStream(new AcceptingPetriNetImpl(net, new Marking()), pnml);
        Map<String, byte[]> entries = new LinkedHashMap<>();
        entries.put("handmade.pnml", pnml.toByteArray());
        File zip = writeZip("handmade.zip", entries);

        // when
        LPMResult result = new ZipLPMImporter().read(Files.newInputStream(zip.toPath()), zip.getName());

        // then
        LocalProcessModel lpm = result.getElement(0);
        Assert.assertEquals(3, lpm.getPlaces().size());
        Assert.assertEquals(4, lpm.getTransitions().size());
        Assert.assertEquals(2, lpm.getTransitions().stream()
                .filter(org.processmining.placebasedlpmdiscovery.model.Transition::isInvisible).count());
        Assert.assertNotNull(lpm.getId()); // no id in the file name, so a fresh one is generated
    }

    @Test
    public void givenZipWithoutPnml_whenRead_thenFails() throws Exception {
        Map<String, byte[]> entries = new LinkedHashMap<>();
        entries.put("lpms.csv", "Name\n".getBytes(StandardCharsets.UTF_8));
        File zip = writeZip("lpms.zip", entries);

        try {
            new ZipLPMImporter().read(Files.newInputStream(zip.toPath()), zip.getName());
            Assert.fail("Expected an exception for a zip without pnml files");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void givenFileNames_whenExtractLPMId_thenIdOnlyForExportPattern() {
        Assert.assertEquals("1234-abcd", ZipLPMImporter.extractLPMId("lpms.1234-abcd.pnml"));
        Assert.assertNull(ZipLPMImporter.extractLPMId("handmade.pnml"));
        Assert.assertNull(ZipLPMImporter.extractLPMId("lpms..pnml"));
    }

    @Test
    public void givenImportedScores_whenJsonRoundTrip_thenScoresArePreserved() throws Exception {
        // given
        LocalProcessModel lpm = withScores(MockLPMs.getSequenceLPM_abc(), 0.25, 0.75);
        File zip = export("lpms.zip", lpm);
        LPMResult imported = new ZipLPMImporter().read(Files.newInputStream(zip.toPath()), zip.getName());

        // when
        ByteArrayOutputStream json = new ByteArrayOutputStream();
        imported.export(ExporterFactory.createLPMDiscoveryResultExporter(), json);
        LPMDiscoveryResult reloaded = ImporterFactory.createLPMDiscoveryResultJsonImporter()
                .read(LPMDiscoveryResult.class, new ByteArrayInputStream(json.toByteArray()));

        // then
        LocalProcessModel reloadedLPM = reloaded.getAllLPMs().iterator().next();
        Assert.assertEquals(0.25, score(reloadedLPM, FITTING_WINDOWS), 1e-9);
        Assert.assertEquals(0.75, score(reloadedLPM, TRACE_SUPPORT), 1e-9);
    }

    private File export(String fileName, LocalProcessModel... lpms) throws IOException {
        LPMResult result = new LPMResult();
        result.addAll(Arrays.asList(lpms));
        File zip = new File(tmp.getRoot(), fileName);
        LocalProcessModelUtils.exportResult(result, zip);
        return zip;
    }

    /**
     * Rewrites the zip with its csv replaced by the given content, or removed if the content is null.
     */
    private File replaceCsv(File zip, String csv) throws IOException {
        Map<String, byte[]> entries = new LinkedHashMap<>();
        String csvName = null;
        try (ZipInputStream in = new ZipInputStream(Files.newInputStream(zip.toPath()))) {
            ZipEntry entry;
            while ((entry = in.getNextEntry()) != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer)) != -1)
                    bytes.write(buffer, 0, read);
                if (entry.getName().endsWith(".csv"))
                    csvName = entry.getName();
                else
                    entries.put(entry.getName(), bytes.toByteArray());
            }
        }
        if (csv != null)
            entries.put(csvName, csv.getBytes(StandardCharsets.UTF_8));
        Files.delete(zip.toPath());
        return writeZip(zip.getName(), entries);
    }

    private File writeZip(String fileName, Map<String, byte[]> entries) throws IOException {
        File zip = new File(tmp.getRoot(), fileName);
        try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(zip.toPath()))) {
            for (Map.Entry<String, byte[]> entry : entries.entrySet()) {
                out.putNextEntry(new ZipEntry(entry.getKey()));
                out.write(entry.getValue());
                out.closeEntry();
            }
        }
        return zip;
    }
}
