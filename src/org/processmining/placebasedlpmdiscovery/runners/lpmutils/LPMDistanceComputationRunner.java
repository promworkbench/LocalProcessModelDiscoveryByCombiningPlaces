package org.processmining.placebasedlpmdiscovery.runners.lpmutils;

import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.*;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

public class LPMDistanceComputationRunner {

    public static void main(String[] args) {
        LocalProcessModel lpm1 = LocalProcessModel.from("(W_Wijzigen_contractgegevens " +
                "| O_SELECTED)(W_Wijzigen_contractgegevens | O_SENT)");
        LocalProcessModel lpm2 = LocalProcessModel.from("(A_ACCEPTED | A_CANCELLED)(A_PARTLYSUBMITTED" +
                " | A_ACCEPTED)");
        LocalProcessModel lpm3 = LocalProcessModel.from("(A_ACCEPTED | A_CANCELLED)(A_PARTLYSUBMITTED" +
                " | A_ACCEPTED)(A_PREACCEPTED | A_ACCEPTED)");

        System.out.println(computeEFOverlap(lpm1, lpm2));
        System.out.println(computeEFOverlap(lpm1, lpm3));
        System.out.println(computeEFOverlap(lpm2, lpm3));
        System.out.println(computeEFOverlap(lpm1, lpm1));
        System.out.println(computeTransitionOverlap(lpm1, lpm1));
        System.out.println(computeLanguageOverlap(lpm1, lpm1));
        System.out.println(computeGED(lpm1, lpm1));
        System.out.println(computeNodeMatching(lpm1, lpm1));
    }

    private static double computeEFOverlap(LocalProcessModel one, LocalProcessModel two) {
        EFOverlapModelDistance modelDistance = new EFOverlapModelDistance();
        return modelDistance.calculateDistance(one, two);
    }

    private static double computeTransitionOverlap(LocalProcessModel one, LocalProcessModel two) {
        TransitionLabelModelDistance modelDistance = new TransitionLabelModelDistance();
        return modelDistance.calculateDistance(one, two);
    }

    private static double computeLanguageOverlap(LocalProcessModel one, LocalProcessModel two) {
        TraceMatchingModelDistance modelDistance = new TraceMatchingModelDistance();
        return modelDistance.calculateDistance(one, two);
    }

    private static double computeGED(LocalProcessModel one, LocalProcessModel two) {
        GEDModelDistances modelDistance = new GEDModelDistances();
        return modelDistance.calculateDistance(one, two);
    }

    private static double computeNodeMatching(LocalProcessModel one, LocalProcessModel two) {
        NodeMatchingModelDistance modelDistance = new NodeMatchingModelDistance();
        return modelDistance.calculateDistance(one, two);
    }
}
