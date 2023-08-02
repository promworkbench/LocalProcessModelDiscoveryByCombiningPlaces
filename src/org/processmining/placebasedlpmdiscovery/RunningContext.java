package org.processmining.placebasedlpmdiscovery;

import org.processmining.placebasedlpmdiscovery.analysis.analyzers.Analyzer;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration.LPMFiltrationController;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.LPMEvaluationController;
import org.processmining.placebasedlpmdiscovery.model.interruptible.InterrupterSubject;

public class RunningContext {

    private LPMFiltrationController lpmFiltrationController;
    private LPMEvaluationController lpmEvaluationController;
    private InterrupterSubject interrupterSubject;
    private Analyzer analyzer;

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

    public InterrupterSubject getInterrupterSubject() {
        return interrupterSubject;
    }

    public void setInterrupterSubject(InterrupterSubject interrupterSubject) {
        this.interrupterSubject = interrupterSubject;
    }

    public Analyzer getAnalyzer() {
        return this.analyzer;
    }

    public void setAnalyzer(Analyzer analyzer) {
        this.analyzer = analyzer;
    }
}
