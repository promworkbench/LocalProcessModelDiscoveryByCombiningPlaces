package org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.undecided.Utils;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.replayer.Replayer;
import org.processmining.placebasedlpmdiscovery.utils.HungarianAlgorithm;
import org.processmining.placebasedlpmdiscovery.utils.LevenshteinDistance;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class TraceMatchingModelDistance implements ModelDistance {
    private static double calculateTraceMatchingDistance(List<List<Integer>> paths1, List<List<Integer>> paths2) {
        if (paths1.isEmpty() || paths2.isEmpty()) {
            return 1;
        }
        LevenshteinDistance levDist = new LevenshteinDistance();
        double[][] costMatrix = new double[paths1.size()][paths2.size()];
        for (int i = 0; i < paths1.size(); ++i) {
            for (int j = 0; j < paths2.size(); ++j) {
                // for each pair of traces compute Levenshtein distance
                costMatrix[i][j] = levDist.compute(paths1.get(i), paths2.get(j));
            }
        }

        HungarianAlgorithm hungarianAlgorithm = new HungarianAlgorithm(costMatrix);
        int[] assignments = hungarianAlgorithm.execute();
        double totalCost = 0;
        for (int i = 0; i < assignments.length; ++i) {
            int iAssignment = assignments[i];
            if (iAssignment == -1) {
                continue;
            }
            totalCost += costMatrix[i][iAssignment] / Math.max(paths1.get(i).size(), paths2.get(iAssignment).size());
        }

        return 2 * totalCost / (paths1.size() + paths2.size());
    }

    @Override
    public double calculateDistance(LocalProcessModel lpm1, LocalProcessModel lpm2) {
        Set<String> trLabels = Sets.union(lpm1.getTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet()),
                lpm2.getTransitions().stream().map(Transition::getLabel).collect(Collectors.toSet()));
        Map<String, Integer> trLabelsMap = Utils.getStringsToIntegerMap(trLabels);

        List<List<Integer>> paths1 = Replayer.findAllPaths(10, lpm1).stream()
                .map(path -> path.stream().map(trLabelsMap::get).collect(Collectors.toList()))
                .collect(Collectors.toList());
        List<List<Integer>> paths2 = Replayer.findAllPaths(10, lpm2).stream()
                .map(path -> path.stream().map(trLabelsMap::get).collect(Collectors.toList()))
                .collect(Collectors.toList());

        return calculateTraceMatchingDistance(paths1, paths2);
    }

    @Override
    public double[][] calculatePairwiseDistance(List<LocalProcessModel> lpms) {
        Set<String> trLabels = lpms.stream()
                .map(lpm -> lpm.getTransitions().stream()
                        .map(Transition::getLabel)
                        .collect(Collectors.toSet()))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
        Map<String, Integer> trLabelsMap = Utils.getStringsToIntegerMap(trLabels);

        double[][] distances = new double[lpms.size()][lpms.size()];
        for (int i = 0; i < lpms.size(); ++i) { // fill diagonal
            distances[i][i] = 0;
        }
        for (int i = 0; i < lpms.size(); ++i) { // fill the rest
            for (int j = 0; j < i; ++j) {
                List<List<Integer>> paths1 = Replayer.findAllPaths(10, lpms.get(i)).stream()
                        .map(path -> path.stream().map(trLabelsMap::get).collect(Collectors.toList()))
                        .collect(Collectors.toList());
                List<List<Integer>> paths2 = Replayer.findAllPaths(10, lpms.get(j)).stream()
                        .map(path -> path.stream().map(trLabelsMap::get).collect(Collectors.toList()))
                        .collect(Collectors.toList());

                distances[i][j] = distances[j][i] = calculateTraceMatchingDistance(paths1, paths2);
            }
        }

        return distances;
    }
}
