package org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms;

import org.apache.commons.lang.NotImplementedException;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.algorithms.fpgrowth.placecombination.FPGrowthForPlacesLPMBuildingAlg;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;

public class LPMBuildingAlgFactory {

    private final LPMEvaluationController evaluationController;

    public LPMBuildingAlgFactory(LPMEvaluationController evaluationController) {
        this.evaluationController = evaluationController;
    }

    public LPMBuildingAlg createLPMBuildingAlg(LPMBuildingAlgType type) {
        if (type.equals(LPMBuildingAlgType.FPGrowthForPlaces)) {
            return new FPGrowthForPlacesLPMBuildingAlg(this.evaluationController);
        }
        throw new NotImplementedException("The factory can not create a LPMBuildingAlg for type " + type + ".");
    }
}
