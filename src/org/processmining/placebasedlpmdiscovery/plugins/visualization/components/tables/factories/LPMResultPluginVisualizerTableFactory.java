package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.LPMEvaluationResultId;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.SimpleEvaluationResult;
import org.processmining.placebasedlpmdiscovery.lpmevaluation.results.aggregateoperations.EvaluationResultAggregateOperation;
import org.processmining.placebasedlpmdiscovery.model.LocalProcessModel;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTable;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTableColumnModel;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTableModel;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.visualizers.LocalProcessModelVisualizer;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Comparator;
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

    public PluginVisualizerTable<LocalProcessModel> getPluginVisualizerTable(SerializableCollection<LocalProcessModel> result,
                                                                             JComponent visualizerComponent,
                                                                             UIPluginContext context) {
        // create map of (index, LPM)
        Iterator<LocalProcessModel> lpmIterator = result.getElements().iterator();
        Map<Integer, LocalProcessModel> lpmIndexMap = IntStream
                .range(0, result.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> lpmIterator.next()));

        PluginVisualizerTable<LocalProcessModel> table = new PluginVisualizerTable<>(lpmIndexMap); // create table
        // create table model
        DecimalFormat df = new DecimalFormat("#.###");
        PluginVisualizerTableModel<LocalProcessModel> tableModel = new PluginVisualizerTableModel<>(
                lpmIndexMap,
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
        table.setModel(tableModel); // set the table model
        table.setColumnModel(new PluginVisualizerTableColumnModel()); // set the column model
        table.createDefaultColumnsFromModel(); // create the columns from the model
        ((PluginVisualizerTableColumnModel) table.getColumnModel()).keepOnlyFirstColumn(); // in the beginning show only the first column
        // create sorter for the columns
        table.setRowSorter(new TableRowSorter<PluginVisualizerTableModel<LocalProcessModel>>(
                (PluginVisualizerTableModel<LocalProcessModel>) table.getModel()) {
            @Override
            public Comparator<?> getComparator(int column) {
                if (column == 0)
                    return Comparator.comparingInt(o -> Integer.parseInt((String) o));
                else if (column > 1)
                    return Comparator.comparingDouble(o -> Double.parseDouble((String) o));
                return super.getComparator(column);
            }
        });
        table.setAutoCreateColumnsFromModel(true); // auto create the columns from the model
        table.setFillsViewportHeight(true); // make the table fill all available height
        // set the row selection to single row
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // add selection listener
        table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            if (listSelectionEvent.getValueIsAdjusting()) // if the value is adjusting
                return; // don't do anything

            // if in the visualizer component there is a LPM drawn
            if (visualizerComponent.getComponents().length >= 1)
                visualizerComponent.remove(0); // remove it
            // create the visualizer
            LocalProcessModelVisualizer visualizer = new LocalProcessModelVisualizer();
            // add visualization for the newly selected LPM
            visualizerComponent.add(
                    visualizer.visualize(
                            context,
                            lpmIndexMap.get(table.convertRowIndexToModel(table.getSelectedRow()))),
                    BorderLayout.CENTER);
            visualizerComponent.revalidate(); // revalidate the component
        });
        // select the first row in the beginning
        table.changeSelection(0, 0, false, false);
        return table;
    }
}
