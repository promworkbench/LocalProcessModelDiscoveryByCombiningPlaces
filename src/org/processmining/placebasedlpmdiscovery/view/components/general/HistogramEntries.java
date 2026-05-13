package org.processmining.placebasedlpmdiscovery.view.components.general;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Converts an integer-keyed distribution into labelled entries suitable for {@link HistogramDrawingPanel}.
 *
 * <p>Use {@link #from} when each key should appear as its own bar, and {@link #binned} when the
 * range of keys is large and grouping into ~20 equal-width bins is preferred.
 */
public class HistogramEntries {

    /**
     * Converts a distribution to one entry per key, sorted ascending.
     * Each entry's label is the string representation of the key.
     *
     * @param distribution frequency distribution — key is the measured value, value is how many items have that value
     * @return entries sorted by key, one per distinct key value
     */
    public static List<Map.Entry<String, Integer>> from(Map<Integer, Integer> distribution) {
        return toEntries(distribution, false);
    }

    /**
     * Converts a distribution to binned entries targeting approximately 20 bars.
     * The bin size is computed as {@code ceil((max - min + 1) / 20)}, so the number of bars
     * is at most 20. Each entry's label is {@code "lo-hi"} for the key range it covers.
     * When the range already fits within 20 values the result is identical to {@link #from}.
     *
     * @param distribution frequency distribution — key is the measured value, value is how many items have that value
     * @return binned entries sorted by bin start, at most ~20 entries
     */
    public static List<Map.Entry<String, Integer>> binned(Map<Integer, Integer> distribution) {
        return toEntries(distribution, true);
    }

    private static List<Map.Entry<String, Integer>> toEntries(Map<Integer, Integer> distribution, boolean binned) {
        if (distribution.isEmpty()) return new ArrayList<>();

        int min = distribution.keySet().stream().mapToInt(Integer::intValue).min().getAsInt();
        int max = distribution.keySet().stream().mapToInt(Integer::intValue).max().getAsInt();

        // binSize=1 means no grouping; when binned, size is chosen so at most ~20 bins cover [min, max]
        int binSize = 1;
        if (binned)
            binSize = Math.max(1, (int) Math.ceil((double) (max - min + 1) / 20));

        List<Map.Entry<String, Integer>> result = new ArrayList<>();
        for (int lo = min; lo <= max; lo += binSize) {
            int hi = lo + binSize - 1;
            // sum all keys that fall within [lo, hi]; keys absent from the distribution contribute 0
            int count = 0;
            for (int k = lo; k <= hi; k++)
                count += distribution.getOrDefault(k, 0);
            String label = (binSize == 1) ? String.valueOf(lo) : lo + "-" + hi;
            result.add(new AbstractMap.SimpleEntry<>(label, count));
        }
        return result;
    }
}
