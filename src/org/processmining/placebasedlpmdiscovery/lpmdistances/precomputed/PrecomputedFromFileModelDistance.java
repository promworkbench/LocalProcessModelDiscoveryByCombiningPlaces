package org.processmining.placebasedlpmdiscovery.lpmdistances.precomputed;

import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrecomputedFromFileModelDistance implements ModelDistance {

    private Map<String, Integer> lpmIds;
    private double[][] distance;

    public PrecomputedFromFileModelDistance(PrecomputedFromFileModelDistanceConfig modelDistanceConfig) {
        readDistances(modelDistanceConfig.getFilename());
    }

    private void readDistances(String filename) {
        // Parsed with Commons CSV (the format DistanceComputationRunner writes), so quoted ids are
        // handled. Empty lines are skipped by the DEFAULT format.
        List<String[]> rowList = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(filename), StandardCharsets.UTF_8);
             CSVParser parser = CSVFormat.DEFAULT.parse(reader)) {
            for (CSVRecord record : parser) {
                if (record.size() == 1 && record.get(0).trim().isEmpty()) {
                    continue; // a line with only whitespace
                }
                String[] cells = new String[record.size()];
                for (int i = 0; i < record.size(); i++) {
                    cells[i] = record.get(i);
                }
                rowList.add(cells);
            }
        }
        catch(Exception e){
            throw new RuntimeException("Could not read distances from " + filename, e);
        }

        if (rowList.isEmpty()) {
            throw new IllegalArgumentException("Distance file " + filename + " is empty");
        }

        // The header is: <corner cell>, id_1, ..., id_n
        String[] header = rowList.get(0);
        int n = header.length - 1;
        this.lpmIds = readLpmIds(header, filename);
        checkSquareMatrix(rowList, n, filename);
        checkRowLabels(rowList, header, filename);

        distance = new double[n][n];
        for (int i = 0; i < n; i++) {
            String[] row = rowList.get(i + 1);
            for (int j = 0; j < n; j++) {
                distance[i][j] = parseCell(row[j + 1], i + 1, j + 1, header, filename);
            }
        }
    }

    /**
     * Checks that data row i is labelled with the same LPM id as header column i. The lookups use
     * the header only, so rows in a different order would otherwise silently give wrong distances.
     */
    private static void checkRowLabels(List<String[]> rowList, String[] header, String filename) {
        for (int i = 1; i < rowList.size(); i++) {
            String rowLabel = rowList.get(i)[0];
            if (!rowLabel.equals(header[i])) {
                throw new IllegalArgumentException(String.format(
                        "Distance file %s has data row %d labelled '%s', but header column %d is '%s'; "
                                + "rows must be in the same order as the header",
                        filename, i, rowLabel, i, header[i]));
            }
        }
    }

    /**
     * Parses one matrix cell. Row and column are 1-based positions in the data part of the file.
     */
    private static double parseCell(String cell, int row, int column, String[] header, String filename) {
        try {
            return Double.parseDouble(cell.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format(
                    "Distance file %s has a non-numeric value '%s' at data row %d, column %d (LPM ids '%s' and '%s')",
                    filename, cell, row, column, header[row], header[column]), e);
        }
    }

    /**
     * Maps each LPM id in the header to its column index. A repeated id is rejected: since ids are
     * canonical, a repeat means the same LPM is listed twice, and the file may contradict itself.
     */
    private static Map<String, Integer> readLpmIds(String[] header, String filename) {
        Map<String, Integer> ids = new HashMap<>();
        for (int ind = 0; ind < header.length - 1; ind++) {
            String id = header[ind + 1];
            Integer previous = ids.putIfAbsent(id, ind);
            if (previous != null) {
                throw new IllegalArgumentException(String.format(
                        "Duplicate LPM id '%s' in %s (columns %d and %d)",
                        id, filename, previous + 1, ind + 1));
            }
        }
        return ids;
    }

    /**
     * Checks that there are n data rows, each with a label cell followed by n values.
     */
    private static void checkSquareMatrix(List<String[]> rowList, int n, String filename) {
        int dataRows = rowList.size() - 1;
        if (dataRows != n) {
            throw new IllegalArgumentException(String.format(
                    "Distance file %s is not square: the header has %d LPM ids but there are %d data rows",
                    filename, n, dataRows));
        }
        for (int i = 1; i <= dataRows; i++) {
            int values = rowList.get(i).length - 1;
            if (values != n) {
                throw new IllegalArgumentException(String.format(
                        "Distance file %s is not square: data row %d has %d values, expected %d",
                        filename, i, values, n));
            }
        }
    }

    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        return distance[indexOf(lpm1)][indexOf(lpm2)];
    }

    /**
     * Builds a new matrix for exactly the given LPMs, in the given order. A new array is always
     * returned so that callers cannot modify the internal matrix.
     */
    @Override
    public double[][] calculatePairwiseDistance(List<LocalProcessModel> lpms) {
        int[] indices = new int[lpms.size()]; // makes index lookup cheap
        for (int i = 0; i < lpms.size(); ++i) {
            indices[i] = indexOf(lpms.get(i));
        }

        double[][] result = new double[lpms.size()][lpms.size()];
        for (int i = 0; i < lpms.size(); ++i) {
            for (int j = 0; j < lpms.size(); ++j) {
                result[i][j] = distance[indices[i]][indices[j]];
            }
        }
        return result;
    }

    private int indexOf(LocalProcessModel lpm) {
        Integer index = lpmIds.get(lpm.getShortString());
        if (index == null) {
            throw new IllegalArgumentException("Illegal LPM with id " + lpm.getShortString());
        }
        return index;
    }
}
