package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EvaluationResultAggregateOperation {

    private final Map<LPMEvaluationResultId, Integer> weights;

    public EvaluationResultAggregateOperation() {
        this.weights = new HashMap<>();
    }

    /**
     * Aggregates the results of the input list. If for none of the results a specific weight was
     * set, it will return average of all results in the list
     *
     * @param results: list of results we want to aggregate
     * @return aggregate value of the results
     */
    public double aggregate(List<SimpleEvaluationResult> results) {
        if (results.size() < 1)
            return -1;

        int total = 0;
        double sum = 0;
        for (SimpleEvaluationResult result : results) {
            int weight = weights.getOrDefault(result.getId(), 1);
            sum += weight * result.getNormalizedResult();
            total += weight;
        }
        return sum / total;
    }

    /**
     * Adds a concrete weight for some evaluation result instead of the default 1
     *
     * @param id:     the id of the evaluation result for which we want to have specific weight
     * @param weight: the weight we want to set
     * @return the updated object so that we can stream multiple calls
     */
    public EvaluationResultAggregateOperation addWeight(LPMEvaluationResultId id, int weight) {
        weights.put(id, weight);
        return this;
    }

    /**
     * Nullifying all weights except for one
     *
     * @param chosenId: the id of the evaluation result we want to use
     * @return the updated object so we can stream multiple calls
     */
    public EvaluationResultAggregateOperation useOnlyOne(LPMEvaluationResultId chosenId) {
        for (LPMEvaluationResultId id : LPMEvaluationResultId.values()) {
            if (id.equals(chosenId)) {
                weights.put(id, 1);
            } else {
                weights.put(id, 0);
            }
        }
        return this;
    }
}
