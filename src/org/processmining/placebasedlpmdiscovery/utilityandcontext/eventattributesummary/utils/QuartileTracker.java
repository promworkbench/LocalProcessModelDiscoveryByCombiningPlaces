package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.utils;

import org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.AttributeSummary;

import java.util.Map;

/**
 * Keeps track of the first quartile, the median and the third quartile of values that are added one by one, and
 * writes them as the Q1, MEDIAN and Q3 representation features of an attribute summary.
 */
public class QuartileTracker {

    private final P2QuantileEstimator q1 = new P2QuantileEstimator(0.25);
    private final P2QuantileEstimator median = new P2QuantileEstimator(0.5);
    private final P2QuantileEstimator q3 = new P2QuantileEstimator(0.75);

    /**
     * Exact quantile with linear interpolation between the closest ranks, used when all values are stored.
     *
     * @param sortedValues the values in ascending order
     * @return the p-quantile, or NaN if there are no values
     */
    public static double exactQuantile(double[] sortedValues, double p) {
        if (sortedValues.length == 0) {
            return Double.NaN;
        }
        double h = (sortedValues.length - 1) * p;
        int lower = (int) Math.floor(h);
        int upper = Math.min(lower + 1, sortedValues.length - 1);
        return sortedValues[lower] + (h - lower) * (sortedValues[upper] - sortedValues[lower]);
    }

    /**
     * Adds the value to the estimators and updates the Q1, MEDIAN and Q3 entries in the representation features.
     */
    public void add(double value, Map<String, Number> representationFeatures) {
        q1.add(value);
        median.add(value);
        q3.add(value);
        representationFeatures.put(AttributeSummary.Q1, q1.getQuantile());
        representationFeatures.put(AttributeSummary.MEDIAN, median.getQuantile());
        representationFeatures.put(AttributeSummary.Q3, q3.getQuantile());
    }
}
