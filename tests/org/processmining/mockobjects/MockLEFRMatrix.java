package org.processmining.mockobjects;

import org.apache.commons.math3.util.Pair;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.loganalyzer.LEFRMatrix;

import java.util.Map;

public class MockLEFRMatrix {

    public static LEFRMatrix returning(int countForAll) {
        return new LEFRMatrix(null, 0) {
            @Override
            public void calculateMatrix() {
            }

            @Override
            public int get(String rowName, String colName) {
                return countForAll;
            }
        };
    }

    public static LEFRMatrix returning(Map<Pair<String, String>, Integer> values) {
        return new LEFRMatrix(null, 0) {
            @Override
            public void calculateMatrix() {
            }

            @Override
            public int get(String rowName, String colName) {
                return values.getOrDefault(new Pair<>(rowName, colName), 0);
            }
        };
    }
}
