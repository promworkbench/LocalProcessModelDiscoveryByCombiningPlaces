package org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.GroupedEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.helpers.WindowTotalCounter;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.fpgrowth.LPMTemporaryWindowInfo;

import java.util.List;
import java.util.Map;

public class WindowsEvaluationResult extends GroupedEvaluationResult {

    private static final long serialVersionUID = -7983593681964435984L;

    private final PassageCoverageEvaluationResult passageCoverageEvaluationResult;
    private final FittingWindowsEvaluationResult fittingWindowsEvaluationResult;
    private final TransitionCoverageEvaluationResult transitionCoverageEvaluationResult;
    private final TraceSupportEvaluationResult traceSupportEvaluationResult;

    public WindowsEvaluationResult(LocalProcessModel lpm, int windowSize, Map<String, Integer> labelMap) {
        super(lpm);
        passageCoverageEvaluationResult = new PassageCoverageEvaluationResult(lpm);
        this.addResult(passageCoverageEvaluationResult);
        fittingWindowsEvaluationResult = new FittingWindowsEvaluationResult(lpm, windowSize);
        this.addResult(fittingWindowsEvaluationResult);
        transitionCoverageEvaluationResult = new TransitionCoverageEvaluationResult(lpm, labelMap);
        transitionCoverageEvaluationResult.setTransitionsCount(lpm.getVisibleTransitions().size());
        this.addResult(transitionCoverageEvaluationResult);
        traceSupportEvaluationResult = new TraceSupportEvaluationResult(lpm);
        this.addResult(traceSupportEvaluationResult);
    }

    public void updatePositive(int windowMultiplicity, List<Integer> window, LPMTemporaryWindowInfo lpmTemporaryWindowInfo, Integer traceVariantId) {
        boolean successful = lpmTemporaryWindowInfo != null;
        if (successful) {
            this.transitionCoverageEvaluationResult.updateTransitionCoverageCountMap(lpmTemporaryWindowInfo.getIntegerFiringSequence(), window, windowMultiplicity);
//            passageCoverageEvaluationResult.updatePassageCoverage(lpmTemporaryWindowInfo.getUsedPassages());
            fittingWindowsEvaluationResult.updateCount(windowMultiplicity);
            fittingWindowsEvaluationResult.updateWeightedCount(1.0 * lpmTemporaryWindowInfo.getIntegerFiringSequence().size() * windowMultiplicity / window.size());
            traceSupportEvaluationResult.addTraces(traceVariantId, windowMultiplicity);
        } else { // if the replay was successful
            throw new UnsupportedOperationException("This should be called only when a window is successful");
        }
    }

//    public void updateAfterWindow(int windowMultiplicity, List<Integer> window, List<Integer> firingSequence) {
//        boolean successful = firingSequence != null;
//        if (successful) { // if the replay was successful
//            passageCoverageEvaluationResult.updatePassageCoverage(window);
//            fittingWindowsEvaluationResult.updateCount(windowMultiplicity);
//            fittingWindowsEvaluationResult.updateWeightedCount(1.0 * firingSequence.size() * windowMultiplicity / window.size());
//            transitionCoverageEvaluationResult.updateTransitionCoverageCountMap(firingSequence, window, windowMultiplicity);
//        }
//        fittingWindowsEvaluationResult.updateTotal(windowMultiplicity);
//        transitionCoverageEvaluationResult.updateTotal(window, windowMultiplicity);
//    }

    public void setTotal(WindowTotalCounter counter, Integer totalTraceCount) {
        fittingWindowsEvaluationResult.setTotal(counter.getWindowCount());
        traceSupportEvaluationResult.setTotalTraceCount(totalTraceCount);
//        transitionCoverageEvaluationResult.setTransitionTotalCounts(counter.getTransitionCount());
    }
}
