package org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity;

import com.google.common.collect.Sets;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistance;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.undecided.Utils;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.replayer.Replayer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EFOverlapModelDistance implements ModelDistance {
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

        return calculateEfOverlapDistance(paths1, paths2);
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

                distances[i][j] = distances[j][i] = calculateEfOverlapDistance(paths1, paths2);
            }
        }

        return distances;
    }

    private static double calculateEfOverlapDistance(List<List<Integer>> paths1, List<List<Integer>> paths2) {
        if (paths1.isEmpty() || paths2.isEmpty()) {
            return 1;
        }

        Set<String> ef1 = new HashSet<>();
        for (List<Integer> path : paths1) {
            for (int i = 0; i < path.size(); ++i) {
                for (int j = i + 1; j < path.size(); ++j) {
                    ef1.add(path.get(i) + "-" + path.get(j));
                }
            }
        }

        Set<String> ef2 = new HashSet<>();
        for (List<Integer> path : paths2) {
            for (int i = 0; i < path.size(); ++i) {
                for (int j = i + 1; j < path.size(); ++j) {
                    ef2.add(path.get(i) + "-" + path.get(j));
                }
            }
        }

        return 2.0 * Sets.intersection(ef1, ef2).size() / (ef1.size() + ef2.size());
    }
}
