package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators;

public enum StandardLPMEvaluatorId implements LPMEvaluatorId {
    FittingWindowEvaluator,
    TransitionOverlappingEvaluator,
    PassageRepetitionEvaluator,
    PassageCoverageEvaluator,
    TransitionCoverageEvaluator,
    TraceSupportCountEvaluator,
    EventCoverageEvaluator;
}
