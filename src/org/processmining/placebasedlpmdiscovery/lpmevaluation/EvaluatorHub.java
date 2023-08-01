package org.processmining.placebasedlpmdiscovery.lpmevaluation;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;

public interface EvaluatorHub {

    void registerEvaluator(String key, WindowLPMEvaluator<?> evaluator);
}
