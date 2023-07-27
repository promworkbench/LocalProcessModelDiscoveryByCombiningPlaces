package org.processmining.placebasedlpmdiscovery.model.additionalinfo;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.*;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LPMAdditionalInfo implements Serializable {
    private static final long serialVersionUID = 3593199319792435898L;

    private transient LocalProcessModel lpm;
    private GroupedEvaluationResult evaluationResult;
    private Map<String, LPMEvaluationResult> evalResults;

    public LPMAdditionalInfo() {
        this.evalResults = new HashMap<>();
    }

    public LPMAdditionalInfo(LocalProcessModel lpm) {
        this.lpm = lpm;
        this.evaluationResult = new GroupedEvaluationResult(lpm);
        this.evalResults = new HashMap<>();
    }

    public LPMAdditionalInfo(LPMAdditionalInfo additionalInfo) {
        try {
            this.evaluationResult = additionalInfo.getEvaluationResult().clone();
            this.lpm = additionalInfo.lpm;
            this.evalResults = new HashMap<>(additionalInfo.evalResults);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public GroupedEvaluationResult getEvaluationResult() {
        return evaluationResult;
    }

    public void clearEvaluation() {
        this.evaluationResult = new GroupedEvaluationResult(lpm);
    }

    public boolean existsEvaluationResult(String key) {
        return this.evalResults.containsKey(key);
    }

    public void addEvaluationResult(String key, LPMEvaluationResult evalResult) {
        this.evalResults.put(key, evalResult);
    }

    public void updateEvaluationResults(String key, LPMEvaluationResult evalResult) {
        this.evalResults.put(key, evalResult);
    }

    public Map<String, LPMEvaluationResult> getEvalResults() {
        return evalResults;
    }

    public <T> T getEvaluationResult(String key, Class<T> infoClass) {
        Object info = this.evalResults.get(key);
        if (info == null) {
            return null;
        }
        if (!infoClass.isInstance(info)) {
            throw new IllegalArgumentException("The info for the key " + key + " is of type " + info.getClass() +
                    " while the requested class is " + infoClass);
        }
        return infoClass.cast(info);
    }
}
