package org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.concrete;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.logs.WindowLog;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.lpmevaluators.AbstractLPMEvaluator;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.concrete.WindowsEvaluationResult;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.replayer.Replayer;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class WindowsLPMEvaluator extends AbstractLPMEvaluator<WindowsEvaluationResult> {

    private final int windowSize;
    private final WindowLog evaluationLog;

    public WindowsLPMEvaluator(XLog log, int windowSize) {
        this.windowSize = windowSize;
        this.evaluationLog = new WindowLog(log);
    }

    @Override
    public WindowsEvaluationResult evaluate(LocalProcessModel lpm) {
        // add invisible transitions that are not in the map
        addInvisibleTransitionsFromLPM(lpm);

        // create the result
        WindowsEvaluationResult result = new WindowsEvaluationResult(lpm, windowSize, this.evaluationLog.getLabelMap());

        // map transitions in the lpm into integers
        Set<Integer> mappedLpmTransitions = lpm.getTransitions()
                .stream()
                .map(x -> this.evaluationLog.getLabelMap().get(x.getLabel()))
                .collect(Collectors.toSet());

        // setup replayer
        Replayer replayer = new Replayer(lpm, this.evaluationLog.getLabelMap());

        // iterate through all trace variants
        for (Integer traceVariantId : this.evaluationLog.getTraceVariantIds()) {
            List<Integer> traceVariant = this.evaluationLog.getTraceVariant(traceVariantId);
            int traceVariantCount = this.evaluationLog.getTraceVariantCount(traceVariant);

            // iterate through all windows
            for (Map.Entry<List<Integer>, Integer> entry : this.evaluationLog
                    .getWindowsForTraceVariant(traceVariantId, windowSize).entrySet()) {
                List<Integer> window = entry.getKey(); // get the window
                int windowCount = entry.getValue(); // get the window count
                int windowMultiplicity = windowCount * traceVariantCount;

                // project the window on the set of labels created from the transitions in the lpm
                LinkedList<Integer> projection = project(window, mappedLpmTransitions);
                // try to replay the projection on the local process model
                List<Integer> firingSequence = replayer.replay(projection, Replayer.ReplayerType.BOTH);

                result.updateAfterWindow(windowMultiplicity, projection, firingSequence);
            }
        }
        return result;
    }

    private void addInvisibleTransitionsFromLPM(LocalProcessModel lpm) {
        this.evaluationLog.addInvisibleTransitionsInLabelMap(lpm.getTransitions()
                .stream()
                .map(Transition::getLabel)
                .collect(Collectors.toSet()));
    }

    /**
     * Project the window on the projection set
     *
     * @param window:        the sequence we want to project
     * @param projectionSet: the elements we want to keep
     * @return projected sequence that contains only elements from the projection set
     */
    private LinkedList<Integer> project(List<Integer> window, Set<Integer> projectionSet) {
        LinkedList<Integer> projection = new LinkedList<>(window);
        Predicate<Integer> notContained = n -> !projectionSet.contains(n);
        projection.removeIf(notContained);
        return projection;
    }
}
