package org.processmining.placebasedlpmdiscovery.view.components.lpmsetdisplay;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.StandardLPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.CustomObjectTableModel;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.GenericTextDescribableTableComponent;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.factories.AbstractPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.tableselection.NewLPMSelectedEmittableDataVM;

import javax.swing.*;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LPMResultPluginVisualizerTableFactory extends AbstractPluginVisualizerTableFactory<LocalProcessModel> {

    private final DataCommunicationControllerVM dcVM;

    @Inject
    public LPMResultPluginVisualizerTableFactory(DataCommunicationControllerVM dcVM) {
        this.dcVM = dcVM;
    }

    private static double getResultOrDefault(LocalProcessModel lpm, LPMEvaluationResultId resultId) {
        SimpleEvaluationResult result = lpm.getAdditionalInfo()
                .getEvaluationResult(resultId.name(), SimpleEvaluationResult.class);
        if (result != null)
            return result.getResult();
        return -1;
    }

    @Override
    protected Map<Integer, LocalProcessModel> getIndexObjectMap(Collection<LocalProcessModel> elements) {
        Iterator<LocalProcessModel> lpmIterator = elements.iterator();
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
                        "Trace Support",
                        "Aggregate Result"
                },
                (ind, lpm) -> new Object[]{
                        ind + 1,
                        lpm.getShortString(),
//                        df.format(getResultOrDefault(lpm, LPMEvaluationResultId.TransitionOverlappingEvaluationResult)),
                        df.format(getResultOrDefault(lpm, StandardLPMEvaluationResultId.TransitionCoverageEvaluationResult)),
                        df.format(getResultOrDefault(lpm, StandardLPMEvaluationResultId.FittingWindowsEvaluationResult)),
                        df.format(getResultOrDefault(lpm, StandardLPMEvaluationResultId.PassageCoverageEvaluationResult)),
                        df.format(getResultOrDefault(lpm, StandardLPMEvaluationResultId.PassageRepetitionEvaluationResult)),
                        df.format(getResultOrDefault(lpm, StandardLPMEvaluationResultId.TraceSupportEvaluationResult)),
                        df.format(new EvaluationResultAggregateOperation().aggregate(lpm.getAdditionalInfo().getEvaluationResults().values()))
                });
    }

    @Override
    protected JPopupMenu getPopupMenu(GenericTextDescribableTableComponent<LocalProcessModel> table) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem exportItem = new JMenuItem("Export");
        exportItem.addActionListener(e -> {
            LPMResult res = new LPMResult();
            for (Integer ind : table.getSelectedRows()) {
                res.add(table.getIndexMap().get(table.convertRowIndexToModel(ind)));
            }
            this.listener.export(res);
        });
        popupMenu.add(exportItem);
        return popupMenu;
    }

    @Override
    protected void newSelection(LocalProcessModel selectedObject) {
        this.dcVM.emit(new NewLPMSelectedEmittableDataVM(selectedObject));
    }
}
