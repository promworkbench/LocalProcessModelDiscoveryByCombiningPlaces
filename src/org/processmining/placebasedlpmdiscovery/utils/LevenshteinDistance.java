package org.processmining.placebasedlpmdiscovery.utils;

import java.util.Arrays;
import java.util.List;

/**
 * Taken from <a href="https://www.geeksforgeeks.org/java-program-to-implement-levenshtein-distance-computing-algorithm/">here</a>
 */
public class LevenshteinDistance {

    public int compute(List<Integer> list1, List<Integer> list2) {
        // A 2-D matrix to store previously calculated
        // answers of sub-problems in order
        // to obtain the final

        int[][] dp = new int[list1.size() + 1][list2.size() + 1];

        for (int i = 0; i <= list1.size(); i++)
        {
            for (int j = 0; j <= list2.size(); j++) {

                // If str1 is empty, all characters of
                // str2 are inserted into str1, which is of
                // the only possible method of conversion
                // with minimum operations.
                if (i == 0) {
                    dp[i][j] = j;
                }

                // If str2 is empty, all characters of str1
                // are removed, which is the only possible
                //  method of conversion with minimum
                //  operations.
                else if (j == 0) {
                    dp[i][j] = i;
                }

                else {
                    // find the minimum among three
                    // operations below


                    dp[i][j] = minm_edits(dp[i - 1][j - 1]
                                    + NumOfReplacement(list1.get(i - 1), list2.get(j - 1)), // replace
                            dp[i - 1][j] + 1, // delete
                            dp[i][j - 1] + 1); // insert
                }
            }
        }

        return dp[list1.size()][list2.size()];
    }

    // check for distinct characters
    // in str1 and str2

    static int NumOfReplacement(int c1, int c2)
    {
        return c1 == c2 ? 0 : 1;
    }

    // receives the count of different
    // operations performed and returns the
    // minimum value among them.

    static int minm_edits(int... nums)
    {
        return Arrays.stream(nums).min().orElse(
                Integer.MAX_VALUE);
    }
}
