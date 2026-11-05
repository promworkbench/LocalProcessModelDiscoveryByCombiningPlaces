package org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.processmining.mockobjects.MockPlaces;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;

public class PrecomputedFromFileModelDistanceTest {

    private static final double DELTA = 1e-9;

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();

    private LocalProcessModel lpmA;
    private LocalProcessModel lpmB;
    private LocalProcessModel lpmC;
    private String a;
    private String b;
    private String c;

    @Before
    public void setUp() {
        lpmA = new LocalProcessModel(MockPlaces.getSequencePlace_ab());
        lpmB = new LocalProcessModel(MockPlaces.getSequencePlace_bc());
        lpmC = new LocalProcessModel(MockPlaces.getChoiceOutPlace());
        a = lpmA.getShortString();
        b = lpmB.getShortString();
        c = lpmC.getShortString();
    }

    private PrecomputedFromFileModelDistance load(String... lines) throws IOException {
        File file = tmp.newFile();
        Files.write(file.toPath(), Arrays.asList(lines), StandardCharsets.UTF_8);
        return new PrecomputedFromFileModelDistance(
                new PrecomputedFromFileModelDistanceConfig(file.getAbsolutePath(), null));
    }

    private PrecomputedFromFileModelDistance loadValid() throws IOException {
        return load(
                "id," + a + "," + b + "," + c,
                a + ",0,1,2",
                b + ",1,0,3",
                c + ",2,3,0");
    }

    private static void assertLoadFails(String expectedMessagePart, ThrowingLoad load) throws IOException {
        try {
            load.run();
            Assert.fail("Expected an IllegalArgumentException containing: " + expectedMessagePart);
        } catch (IllegalArgumentException e) {
            Assert.assertTrue("Unexpected message: " + e.getMessage(),
                    e.getMessage().contains(expectedMessagePart));
        }
    }

    private interface ThrowingLoad {
        void run() throws IOException;
    }

    // ---- calculateDistance ----

    @Test
    public void calculateDistance_returnsValueForThePairNotTheDiagonal() throws IOException {
        PrecomputedFromFileModelDistance distance = loadValid();

        Assert.assertEquals(1.0, distance.calculateDistance(lpmA, lpmB), DELTA);
        Assert.assertEquals(3.0, distance.calculateDistance(lpmB, lpmC), DELTA);
        Assert.assertEquals(0.0, distance.calculateDistance(lpmA, lpmA), DELTA);
    }

    @Test
    public void calculateDistance_unknownLpmThrowsIllegalArgument() throws IOException {
        PrecomputedFromFileModelDistance distance = load(
                "id," + a + "," + b,
                a + ",0,1",
                b + ",1,0");

        assertLoadFails("Illegal LPM with id", () -> distance.calculateDistance(lpmA, lpmC));
    }

    // ---- calculatePairwiseDistance ----

    @Test
    public void calculatePairwiseDistance_subsetHasRightSizeAndOrder() throws IOException {
        PrecomputedFromFileModelDistance distance = loadValid();

        double[][] result = distance.calculatePairwiseDistance(Arrays.asList(lpmC, lpmA));

        Assert.assertEquals(2, result.length);
        Assert.assertEquals(2, result[0].length);
        Assert.assertEquals(0.0, result[0][0], DELTA);
        Assert.assertEquals(2.0, result[0][1], DELTA);
        Assert.assertEquals(2.0, result[1][0], DELTA);
        Assert.assertEquals(0.0, result[1][1], DELTA);
    }

    @Test
    public void calculatePairwiseDistance_prefixInFileOrderIsNotTheFullMatrix() throws IOException {
        PrecomputedFromFileModelDistance distance = loadValid();

        double[][] result = distance.calculatePairwiseDistance(Arrays.asList(lpmA, lpmB));

        Assert.assertEquals(2, result.length);
        Assert.assertEquals(1.0, result[0][1], DELTA);
    }

    @Test
    public void calculatePairwiseDistance_returnsCopyNotInternalMatrix() throws IOException {
        PrecomputedFromFileModelDistance distance = loadValid();

        double[][] first = distance.calculatePairwiseDistance(Arrays.asList(lpmA, lpmB, lpmC));
        first[0][1] = 99.0;

        Assert.assertEquals(1.0, distance.calculateDistance(lpmA, lpmB), DELTA);
    }

    @Test
    public void calculatePairwiseDistance_unknownLpmThrowsIllegalArgument() throws IOException {
        PrecomputedFromFileModelDistance distance = load(
                "id," + a + "," + b,
                a + ",0,1",
                b + ",1,0");

        assertLoadFails("Illegal LPM with id",
                () -> distance.calculatePairwiseDistance(Collections.singletonList(lpmC)));
    }

    // ---- CSV parsing ----

    @Test
    public void load_quotedIdWithCommaDoesNotShiftColumns() throws IOException {
        PrecomputedFromFileModelDistance distance = load(
                "id,\"x,y\"," + a + "," + b,
                "\"x,y\",0,5,6",
                a + ",5,0,1",
                b + ",6,1,0");

        Assert.assertEquals(1.0, distance.calculateDistance(lpmA, lpmB), DELTA);
        Assert.assertEquals(0.0, distance.calculateDistance(lpmA, lpmA), DELTA);
    }

    @Test
    public void load_quotedIdsAreUnquoted() throws IOException {
        PrecomputedFromFileModelDistance distance = load(
                "id,\"" + a + "\",\"" + b + "\"",
                "\"" + a + "\",0,1",
                "\"" + b + "\",1,0");

        Assert.assertEquals(1.0, distance.calculateDistance(lpmA, lpmB), DELTA);
    }

    @Test
    public void load_readsFileInTheFormatDistanceComputationRunnerWrites() throws IOException {
        // Same CSVFormat settings as DistanceComputationRunner.writeDistances
        File file = tmp.newFile();
        try (CSVPrinter csv = CSVFormat.DEFAULT.builder()
                .setHeader("LPM", a, b)
                .build()
                .print(file.toPath(), StandardCharsets.UTF_8)) {
            csv.printRecord(a, 0.0, 0.25);
            csv.printRecord(b, 0.25, 0.0);
        }

        PrecomputedFromFileModelDistance distance = new PrecomputedFromFileModelDistance(
                new PrecomputedFromFileModelDistanceConfig(file.getAbsolutePath(), null));

        Assert.assertEquals(0.25, distance.calculateDistance(lpmA, lpmB), DELTA);
    }

    @Test
    public void load_utf8ByteOrderMarkIsHarmless() throws IOException {
        PrecomputedFromFileModelDistance distance = load(
                "﻿id," + a + "," + b,
                a + ",0,1",
                b + ",1,0");

        Assert.assertEquals(1.0, distance.calculateDistance(lpmA, lpmB), DELTA);
    }

    // ---- file validation ----

    @Test
    public void load_trailingBlankLineIsIgnored() throws IOException {
        PrecomputedFromFileModelDistance distance = load(
                "id," + a + "," + b,
                a + ",0,1",
                b + ",1,0",
                "");

        Assert.assertEquals(1.0, distance.calculateDistance(lpmA, lpmB), DELTA);
    }

    @Test
    public void load_duplicateIdIsRejected() {
        assertLoadFailsUnchecked("Duplicate LPM id '" + a + "'",
                "id," + a + "," + b + "," + a,
                a + ",0,1,2",
                b + ",1,0,3",
                a + ",2,3,0");
    }

    @Test
    public void load_fewerRowsThanIdsIsRejected() {
        assertLoadFailsUnchecked("the header has 3 LPM ids but there are 2 data rows",
                "id," + a + "," + b + "," + c,
                a + ",0,1,2",
                b + ",1,0,3");
    }

    @Test
    public void load_shortRowIsRejected() {
        assertLoadFailsUnchecked("data row 2 has 2 values, expected 3",
                "id," + a + "," + b + "," + c,
                a + ",0,1,2",
                b + ",1,0",
                c + ",2,3,0");
    }

    @Test
    public void load_rowsInDifferentOrderThanHeaderAreRejected() {
        assertLoadFailsUnchecked("data row 1 labelled '" + b + "', but header column 1 is '" + a + "'",
                "id," + a + "," + b,
                b + ",1,0",
                a + ",0,1");
    }

    @Test
    public void load_emptyFileIsRejected() {
        assertLoadFailsUnchecked("is empty");
    }

    @Test
    public void load_nonNumericCellIsRejectedWithPosition() {
        assertLoadFailsUnchecked("non-numeric value 'x' at data row 2, column 3",
                "id," + a + "," + b + "," + c,
                a + ",0,1,2",
                b + ",1,0,x",
                c + ",2,3,0");
    }

    private void assertLoadFailsUnchecked(String expectedMessagePart, String... lines) {
        try {
            assertLoadFails(expectedMessagePart, () -> load(lines));
        } catch (IOException e) {
            throw new AssertionError("Could not write the test file", e);
        }
    }
}
