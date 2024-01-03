package org.processmining.placebasedlpmdiscovery.lpmevaluation;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMCollector;

public interface EvaluatorHub {

    void registerEvaluator(String key, WindowLPMCollector<?> evaluator);
}
