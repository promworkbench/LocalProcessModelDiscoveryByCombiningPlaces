package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories;

import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.CustomObjectTableModel;

import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LPMResultPluginVisualizerTableFactory extends AbstractPluginVisualizerTableFactory<LocalProcessModel> {

    private static double getResultOrDefault(LocalProcessModel lpm, LPMEvaluationResultId resultId) {
        SimpleEvaluationResult result = lpm.getAdditionalInfo().getEvaluationResult().getEvaluationResult(resultId);
        if (result != null)
            return result.getResult();
        return -1;
    }

    @Override
    protected Map<Integer, LocalProcessModel> getIndexObjectMap(SerializableCollection<LocalProcessModel> elements) {
        Iterator<LocalProcessModel> lpmIterator = elements.getElements().iterator();
        return IntStream
                .range(0, elements.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> lpmIterator.next()));
    }

    @Override
    protected CustomObjectTableModel<LocalProcessModel> createTableModel(Map<Integer, LocalProcessModel> indexObjectMap) {
        DecimalFormat df = new DecimalFormat("#.###");
        return new CustomObjectTableModel<>(
                indexObjectMap,
                new String[]{
                        "LPM Index",
                        "LPM Short Name",
//                        "Transition Overlapping Score",
                        "Transition Coverage Score",
                        "Fitting Window Score",
                        "Passage Coverage Score",
                        "Passage Repetition Score",
                        "Aggregate Result"
                },
                (ind, lpm) -> new Object[]{
                        ind + 1,
                        lpm.getShortString(),
//                        df.format(getResultOrDefault(lpm, LPMEvaluationResultId.TransitionOverlappingEvaluationResult)),
                        df.format(getResultOrDefault(lpm, LPMEvaluationResultId.TransitionCoverageEvaluationResult)),
                        df.format(getResultOrDefault(lpm, LPMEvaluationResultId.FittingWindowsEvaluationResult)),
                        df.format(getResultOrDefault(lpm, LPMEvaluationResultId.PassageCoverageEvaluationResult)),
                        df.format(getResultOrDefault(lpm, LPMEvaluationResultId.PassageRepetitionEvaluationResult)),
                        df.format(lpm.getAdditionalInfo().getEvaluationResult()
                                .getResult(new EvaluationResultAggregateOperation()))
                });
    }
}
