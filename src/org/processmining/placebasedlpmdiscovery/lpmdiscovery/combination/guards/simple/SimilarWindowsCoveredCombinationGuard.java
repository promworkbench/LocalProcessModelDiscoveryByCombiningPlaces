package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple;//package org.processmining.placebasedlpmdiscovery.lpmdiscovery.combination.guards.simple;
//
//import com.google.common.collect.Sets;
//import org.processmining.placebasedlpmdiscovery.evaluation.results.concrete.WindowsEvaluationResult;
//import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
//
//public class SimilarWindowsCoveredCombinationGuard extends SimpleCombinationGuard {
//
//    private double threshold;
//
//    public SimilarWindowsCoveredCombinationGuard(double threshold) {
//        this.threshold = threshold;
//    }
//
//    @Override
//    public boolean satisfies(LocalProcessModel lpm1, LocalProcessModel lpm2) {
//        int intersectionSize = Sets.intersection(
//                lpm1.getAdditionalInfo().getEvaluationResult().getSimpleEvaluationResult(WindowsEvaluationResult.class).getCoveredWindowsLabels(),
//                lpm2.getAdditionalInfo().getEvaluationResult().getSimpleEvaluationResult(WindowsEvaluationResult.class).getCoveredWindowsLabels())
//                .size();
//        int smallerSetSize = Math.min(
//                lpm1.getAdditionalInfo().getEvaluationResult().getSimpleEvaluationResult(WindowsEvaluationResult.class).getCoveredWindowsLabels().size(),
//                lpm2.getAdditionalInfo().getEvaluationResult().getSimpleEvaluationResult(WindowsEvaluationResult.class).getCoveredWindowsLabels().size());
//
//        return (intersectionSize * 1.0 / smallerSetSize) > threshold;
//    }
//}
