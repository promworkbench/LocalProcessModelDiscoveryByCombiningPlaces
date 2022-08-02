package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTable;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTableColumnModel;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTableModel;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.TableListener;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.visualizers.PlaceVisualizer;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlaceSetPluginVisualizerTableFactory extends AbstractPluginVisualizerTableFactory<Place> {

    public PluginVisualizerTable<Place> getPluginVisualizerTable(SerializableCollection<Place> result,
                                                                 TableListener<Place> listener,
                                                                 UIPluginContext context) {
        // create map of (index, LPM)
        final Iterator<Place> placeIterator = result.getElements().iterator();
        Map<Integer, Place> placeIndexMap = IntStream
                .range(0, result.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> placeIterator.next()));

        PluginVisualizerTable<Place> table = new PluginVisualizerTable<>(placeIndexMap); // create table
        // create mode
        PluginVisualizerTableModel<Place> tableModel = new PluginVisualizerTableModel<>(
                placeIndexMap,
                new String[]{"Place Index", "Place Short Name"},
                (ind, place) -> new Object[]{
                        ind + 1,
                        place.getShortString()
                });
        table.setModel(tableModel); // set the table model
        table.setColumnModel(new PluginVisualizerTableColumnModel()); // set the column model
        // create sorter for the columns
        table.setRowSorter(new TableRowSorter<PluginVisualizerTableModel<Place>>(
                (PluginVisualizerTableModel<Place>) table.getModel()) {
            @Override
            public Comparator<?> getComparator(int column) {
                if (column == 0)
                    return Comparator.comparingInt(o -> Integer.parseInt((String) o));
                return super.getComparator(column);
            }
        });
        table.createDefaultColumnsFromModel(); // create the columns from the model
        ((PluginVisualizerTableColumnModel) table.getColumnModel()).keepOnlyFirstColumn(); // in the beginning show only the first column
        table.setAutoCreateColumnsFromModel(true); // auto create the columns from the model
        table.setFillsViewportHeight(true); // make the table fill all available height
        // set the row selection to single row
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // add selection listener
        table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
            if (listSelectionEvent.getValueIsAdjusting()) // if the value is adjusting
                return; // don't do anything

            listener.newSelection(placeIndexMap.get(table.convertRowIndexToModel(table.getSelectedRow())));
        });
        // select the first row in the beginning
        table.changeSelection(0, 0, false, false);
        return table;
    }

}
