package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.model.TextDescribable;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTable;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTableColumnModel;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTableModel;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.TableListener;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;

public abstract class AbstractPluginVisualizerTableFactory<T extends TextDescribable & Serializable> {

    public PluginVisualizerTable<T> getPluginVisualizerTable(SerializableCollection<T> result,
                                                                      TableListener<T> listener,
                                                                      UIPluginContext context) {

        // create table
        Map<Integer, T> indexObjectMap = getIndexObjectMap(result);
        PluginVisualizerTable<T> table = new PluginVisualizerTable<>(indexObjectMap);

        // set table model
        PluginVisualizerTableModel<T> tableModel = createTableModel(indexObjectMap);
        table.setModel(tableModel); // set the table model
        table.setColumnModel(new PluginVisualizerTableColumnModel()); // set the column model
        table.createDefaultColumnsFromModel(); // create the columns from the model
        ((PluginVisualizerTableColumnModel) table.getColumnModel()).keepOnlyFirstColumn(); // in the beginning show only the first column
        table.setRowSorter(new TableRowSorter<PluginVisualizerTableModel<T>>(
                tableModel) {
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

            listener.newSelection(indexObjectMap.get(table.convertRowIndexToModel(table.getSelectedRow())));
        });
        // select the first row in the beginning
        table.changeSelection(0, 0, false, false);
        return table;
    }

    protected abstract Map<Integer,T> getIndexObjectMap(SerializableCollection<T> elements);

    protected abstract PluginVisualizerTableModel<T> createTableModel(Map<Integer, T> indexObjectMap);

}
