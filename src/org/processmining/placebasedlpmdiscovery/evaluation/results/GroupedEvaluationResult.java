package org.processmining.placebasedlpmdiscovery.evaluation.results;

import org.processmining.placebasedlpmdiscovery.evaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;

import java.util.ArrayList;
import java.util.List;

public class GroupedEvaluationResult extends AbstractEvaluationResult {

    private static final long serialVersionUID = -5013843797143478337L;

    private List<SimpleEvaluationResult> results;

    public GroupedEvaluationResult(LocalProcessModel lpm) {
        super(lpm);
        this.results = new ArrayList<>();
    }

    public void addResult(AbstractEvaluationResult result) {
        if (result instanceof SimpleEvaluationResult) {
            SimpleEvaluationResult castResult = (SimpleEvaluationResult) result;
            this.results.add(castResult);
        } else if (result instanceof GroupedEvaluationResult) {
            GroupedEvaluationResult castResult = (GroupedEvaluationResult) result;
            this.results.addAll(castResult.getResults());
        } else {
            throw new UnsupportedOperationException("Unknown subclass of AbstractEvaluationResult: " + result.getClass());
        }
    }

    public List<SimpleEvaluationResult> getResults() {
        return results;
    }

    public void setResults(List<SimpleEvaluationResult> results) {
        this.results = results;
    }

    public <T extends SimpleEvaluationResult> T getSimpleEvaluationResult(Class<? extends T> c) {
        for (SimpleEvaluationResult result : results) {
            if (result.getClass() == c) {
                return c.cast(result);
            }
        }
        return null;
    }

    public SimpleEvaluationResult getEvaluationResult(LPMEvaluationResultId evaluationId) {
        for (SimpleEvaluationResult result : results) {
            if (result.getId() == evaluationId) {
                return result;
            }
        }
        return null;
    }

    public double getResult(EvaluationResultAggregateOperation operation) {
        return operation.aggregate(this.results);
    }

    @Override
    public GroupedEvaluationResult clone() throws CloneNotSupportedException {
        GroupedEvaluationResult clone = (GroupedEvaluationResult) super.clone();
        clone.results = new ArrayList<>();
        for (SimpleEvaluationResult result : this.results) {
            clone.addResult(result.clone());
        }
        return clone;
    }
}
