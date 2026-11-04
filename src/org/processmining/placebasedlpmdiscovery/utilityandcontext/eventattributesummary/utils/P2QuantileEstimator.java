package org.processmining.placebasedlpmdiscovery.utilityandcontext.eventattributesummary.utils;

import java.util.Arrays;

/**
 * Streaming estimator of a single quantile using the P² algorithm (Jain & Chlamtac, 1985). It keeps five markers
 * (the minimum, the p/2, p and (1+p)/2 quantiles and the maximum) whose heights are adjusted with a piecewise
 * parabolic formula after each observation, so it uses constant memory independent of the number of values.
 * The first {@link #EXACT_LIMIT} observations are buffered and the quantile is computed exactly from them; the markers
 * are then initialized from the exact quantiles of the buffer. The original algorithm initializes them from the first
 * five observations, which makes all quantiles equal to the median of those five and converges slowly afterwards.
 */
public class P2QuantileEstimator {

    private static final int EXACT_LIMIT = 32;

    private final double p;
    private final double[] heights = new double[5]; // marker heights q_i
    private final int[] positions = new int[5]; // actual marker positions n_i (1-based)
    private final double[] desiredPositions = new double[5]; // desired marker positions n'_i
    private final double[] increments; // increments of the desired positions dn'_i
    private double[] buffer = new double[EXACT_LIMIT]; // the first observations, released once the markers are set
    private int count;

    public P2QuantileEstimator(double p) {
        if (p <= 0 || p >= 1) {
            throw new IllegalArgumentException("The quantile should be in (0, 1) but is " + p);
        }
        this.p = p;
        this.increments = new double[]{0, p / 2, p, (1 + p) / 2, 1};
    }

    public void add(double value) {
        if (count < EXACT_LIMIT) {
            buffer[count++] = value;
            if (count == EXACT_LIMIT) {
                initializeMarkers();
            }
            return;
        }
        count++;

        // find the cell k such that heights[k] <= value < heights[k + 1], extending the extremes if needed
        int k;
        if (value < heights[0]) {
            heights[0] = value;
            k = 0;
        } else if (value >= heights[4]) {
            heights[4] = value;
            k = 3;
        } else {
            k = 0;
            while (value >= heights[k + 1]) {
                k++;
            }
        }

        for (int i = k + 1; i < 5; ++i) {
            positions[i]++;
        }
        for (int i = 0; i < 5; ++i) {
            desiredPositions[i] += increments[i];
        }

        // adjust the heights of the middle markers if they are off from their desired positions
        for (int i = 1; i < 4; ++i) {
            double d = desiredPositions[i] - positions[i];
            if ((d >= 1 && positions[i + 1] - positions[i] > 1) || (d <= -1 && positions[i - 1] - positions[i] < -1)) {
                int s = d >= 0 ? 1 : -1;
                double candidate = parabolic(i, s);
                heights[i] = heights[i - 1] < candidate && candidate < heights[i + 1] ? candidate : linear(i, s);
                positions[i] += s;
            }
        }
    }

    /**
     * Places the markers at the desired positions for the buffered observations, i.e. the ranks of the minimum, the
     * p/2, p and (1+p)/2 quantiles and the maximum, with the heights of the observations at those ranks.
     */
    private void initializeMarkers() {
        Arrays.sort(buffer);
        for (int i = 0; i < 5; ++i) {
            desiredPositions[i] = 1 + (count - 1) * increments[i];
            // positions have to be strictly increasing and leave room for the markers after it
            positions[i] = Math.min(Math.max((int) Math.round(desiredPositions[i]), i == 0 ? 1 : positions[i - 1] + 1),
                    count - 4 + i);
            heights[i] = buffer[positions[i] - 1];
        }
        buffer = null;
    }

    private double parabolic(int i, int s) {
        return heights[i] + (double) s / (positions[i + 1] - positions[i - 1]) *
                ((positions[i] - positions[i - 1] + s) * (heights[i + 1] - heights[i]) / (positions[i + 1] - positions[i]) +
                        (positions[i + 1] - positions[i] - s) * (heights[i] - heights[i - 1]) / (positions[i] - positions[i - 1]));
    }

    private double linear(int i, int s) {
        return heights[i] + s * (heights[i + s] - heights[i]) / (positions[i + s] - positions[i]);
    }

    /**
     * @return the estimated quantile, or NaN if no value has been added
     */
    public double getQuantile() {
        if (count == 0) {
            return Double.NaN;
        }
        if (count >= EXACT_LIMIT) {
            return heights[2];
        }
        // exact quantile with linear interpolation between the closest ranks
        double[] sorted = Arrays.copyOf(buffer, count);
        Arrays.sort(sorted);
        double h = (count - 1) * p;
        int lower = (int) Math.floor(h);
        int upper = Math.min(lower + 1, count - 1);
        return sorted[lower] + (h - lower) * (sorted[upper] - sorted[lower]);
    }

    public int getCount() {
        return count;
    }
}
