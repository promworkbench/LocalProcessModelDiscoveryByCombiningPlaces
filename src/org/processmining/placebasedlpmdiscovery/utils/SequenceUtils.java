package org.processmining.placebasedlpmdiscovery.utils;

import java.util.*;

public class SequenceUtils {

    public static <T> boolean isSubsequence(List<T> sequence, List<T> subsequence) {
        int i = 0;
        int j = 0;

        while (i < sequence.size() && j < subsequence.size()) {
            if (sequence.get(i).equals(subsequence.get(j))) {
                i++;
                j++;
            } else {
                i++;
            }
        }
        return j == subsequence.size();
    }

    public static <T> List<T> joinSubsequences(List<T> one, List<T> two, List<T> superSequence,
                                           boolean addAdditional) {
        int i = 0;
        int j = 0;
        int k = 0;
        Set<T> elementsInSuper = new HashSet<>(superSequence);
        List<T> result = new ArrayList<>();
        while (true) {
            while (i < one.size() && !elementsInSuper.contains(one.get(i))) {
                if (addAdditional) {
                    result.add(one.get(i));
                }
                i++;
            }
            while (j < two.size() && !elementsInSuper.contains(two.get(j))) {
                if (addAdditional) {
                    result.add(two.get(j));
                }
                j++;
            }
            if (i >= one.size() && j >= two.size())
                return result;
            if (i >= one.size()) {
                result.addAll(two.subList(j, two.size()));
                return result;
            }
            if (j >= two.size()) {
                result.addAll(one.subList(i, one.size()));
                return result;
            }
            if (k >= superSequence.size()) {
                return result;
            }
            if (one.get(i).equals(two.get(j)) && one.get(i).equals(superSequence.get(k))) {
                result.add(one.get(i));
                i++;
                j++;
                k++;
            } else if (one.get(i).equals(superSequence.get(k)) && !two.get(j).equals(superSequence.get(k))) {
                result.add(one.get(i));
                i++;
                k++;
            } else if (!one.get(i).equals(superSequence.get(k)) && two.get(j).equals(superSequence.get(k))) {
                result.add(two.get(j));
                j++;
                k++;
            } else {
                k++;
            }
        }
    }
}
