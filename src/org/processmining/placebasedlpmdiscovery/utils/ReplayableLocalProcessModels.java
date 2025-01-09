package org.processmining.placebasedlpmdiscovery.utils;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.ReplayableLocalProcessModel;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ReplayableLocalProcessModels {

    public static ReplayableLocalProcessModel join(ReplayableLocalProcessModel lpm1, ReplayableLocalProcessModel lpm2) {
        ReplayableLocalProcessModel result = new ReplayableLocalProcessModel();
        for (Integer constraintId : lpm1.getConstraintMap().keySet()) {
            copyConstraintWhenNotPresent(constraintId, lpm1, result);
        }
        for (Integer constraintId : lpm2.getConstraintMap().keySet()) {
            copyConstraintWhenNotPresent(constraintId, lpm2, result);
        }
        return result;
    }

    public static void copyConstraintWhenNotPresent(Integer constraintId, ReplayableLocalProcessModel from,
                                                    ReplayableLocalProcessModel to) {
        String id = from.getConstraintMap().get(constraintId);
        if (to.getConstraintMap().containsValue(id)) {
            return;
        }

        Set<Integer> haveAsInputConstraint = from.getInputConstraints().entrySet().stream()
                .filter(entry -> entry.getValue().contains(constraintId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        Set<Integer> haveAsOutputConstraint = from.getOutputConstraints().entrySet().stream()
                .filter(entry -> entry.getValue().contains(constraintId))
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        to.addConstraint(id, from.getNumTokens(constraintId), haveAsInputConstraint, haveAsOutputConstraint);
    }
}
