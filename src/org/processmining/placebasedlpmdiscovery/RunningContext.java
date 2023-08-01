package org.processmining.placebasedlpmdiscovery;

import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;

public class RunningContext {

    private LPMFiltrationController lpmFiltrationController;
    private LPMEvaluationController lpmEvaluationController;

    public void setLpmFiltrationController(LPMFiltrationController lpmFiltrationController) {
        this.lpmFiltrationController = lpmFiltrationController;
    }

    public LPMFiltrationController getLpmFiltrationController() {
        return lpmFiltrationController;
    }

    public LPMEvaluationController getLpmEvaluationController() {
        return lpmEvaluationController;
    }

    public void setLpmEvaluationController(LPMEvaluationController lpmEvaluationController) {
        this.lpmEvaluationController = lpmEvaluationController;
    }
}
