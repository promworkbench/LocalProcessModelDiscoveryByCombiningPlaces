package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;

import java.util.Collection;
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
    public double aggregate(Collection<LPMEvaluationResult> results) {
        if (results.size() < 1)
            return -1;

        int total = 0;
        double sum = 0;
        for (LPMEvaluationResult result : results) {
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
}
