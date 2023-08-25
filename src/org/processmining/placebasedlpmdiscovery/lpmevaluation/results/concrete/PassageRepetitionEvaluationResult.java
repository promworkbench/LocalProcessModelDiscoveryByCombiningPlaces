package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.additionalinfo.Passage;

import java.util.Map;

public class PassageRepetitionEvaluationResult extends SimpleEvaluationResult {

    private static final long serialVersionUID = -13763990802711904L;

    private final Map<Passage, Integer> passageRepetitionMap;
    private double result;

    public PassageRepetitionEvaluationResult(LocalProcessModel lpm, Map<Passage, Integer> passageRepetitionMap) {
        super(lpm, StandardLPMEvaluationResultId.PassageRepetitionEvaluationResult);
        this.passageRepetitionMap = passageRepetitionMap;
        this.result = -1;
    }

    private void calculateResult() {
        int max = passageRepetitionMap.size() * this.lpm.getPlaces().size();
        int min = passageRepetitionMap.size();
        int actual = passageRepetitionMap.values().stream().mapToInt(x -> x).sum();

        this.result = 1.0 * (max - actual) / (max - min);
    }

    @Override
    public double getResult() {
        if (this.result == -1)
            calculateResult();
        return this.result;
    }

    @Override
    public double getNormalizedResult() {
        return this.getResult();
    }
}
