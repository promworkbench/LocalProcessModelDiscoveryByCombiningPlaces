package org.processmining.placebasedlpmdiscovery;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationAndEvaluationController;

public class RunningContext {

    LPMFiltrationAndEvaluationController lpmFiltrationAndEvaluationController;

    public LPMFiltrationAndEvaluationController getLpmFiltrationAndEvaluationController() {
        return lpmFiltrationAndEvaluationController;
    }

    public void setLpmFiltrationAndEvaluationController(LPMFiltrationAndEvaluationController lpmFiltrationAndEvaluationController) {
        this.lpmFiltrationAndEvaluationController = lpmFiltrationAndEvaluationController;
    }
}
