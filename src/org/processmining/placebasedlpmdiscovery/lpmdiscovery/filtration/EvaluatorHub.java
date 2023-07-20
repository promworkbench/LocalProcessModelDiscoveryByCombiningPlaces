package org.processmining.placebasedlpmdiscovery.lpmdiscovery.filtration;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.WindowLPMEvaluator;

public interface EvaluatorHub {

    void registerEvaluator(WindowLPMEvaluator<?> evaluator);
}
