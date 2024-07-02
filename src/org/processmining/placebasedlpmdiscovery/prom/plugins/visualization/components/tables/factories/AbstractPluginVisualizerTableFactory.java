package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories;

import org.processmining.placebasedlpmdiscovery.model.TextDescribable;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.CustomObjectTableModel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.GenericTextDescribableTableComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.TableListener;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.VisibilityControllableTableColumnModel;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;

public abstract class AbstractPluginVisualizerTableFactory<T extends TextDescribable & Serializable> implements PluginVisualizerTableFactory<T> {

    protected GenericTextDescribableTableComponent<T> table;

    protected TableListener<T> listener;

    @Override
    public GenericTextDescribableTableComponent<T> getPluginVisualizerTable(Collection<T> result, TableListener<T> listener) {
        this.listener = listener;

        // create table
        Map<Integer, T> indexObjectMap = getIndexObjectMap(result);
        table = new GenericTextDescribableTableComponent<>(indexObjectMap);

        // set table model
        CustomObjectTableModel<T> tableModel = createTableModel(indexObjectMap);
        table.setModel(tableModel); // set the table model
        table.setColumnModel(new VisibilityControllableTableColumnModel()); // set the column model
        table.createDefaultColumnsFromModel(); // create the columns from the model
        ((VisibilityControllableTableColumnModel) table.getColumnModel()).keepOnlyFirstColumn(); // in the beginning show only the first column
        table.setRowSorter(new TableRowSorter<CustomObjectTableModel<T>>(
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
        table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // add selection listener
        table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            if (listSelectionEvent.getValueIsAdjusting()) // if the value is adjusting
                return; // don't do anything

            if (table.getSelectedRowCount() == 1) {
                listener.newSelection(indexObjectMap.get(table.convertRowIndexToModel(table.getSelectedRow())));
            }
        });
        // select the first row in the beginning
        table.changeSelection(0, 0, false, false);
        table.setComponentPopupMenu(this.getPopupMenu());
        return table;
    }

    protected abstract Map<Integer,T> getIndexObjectMap(Collection<T> elements);

    protected abstract CustomObjectTableModel<T> createTableModel(Map<Integer, T> indexObjectMap);

    protected abstract JPopupMenu getPopupMenu();

}
