package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables;

import org.processmining.placebasedlpmdiscovery.model.TextDescribable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Map;

public class PluginVisualizerTable<T extends TextDescribable> extends JTable {

    private final Map<Integer, T> indexMap;

    public PluginVisualizerTable(Map<Integer, T> indexMap) {
        this.indexMap = indexMap;
    }

    @Override
    protected JTableHeader createDefaultTableHeader() {
        return new JTableHeader(columnModel) {
            @Override
            public String getToolTipText(MouseEvent event) {
                Point p = event.getPoint();
                int index = columnModel.getColumnIndexAtX(p.x);
                return columnModel.getColumn(index).getHeaderValue() + "    (Click in order to sort)";
            }
        };
    }

    @Override
    public String getToolTipText(MouseEvent event) {
        int column = this.columnAtPoint(event.getPoint());

        if (column != 1)
            return null;

        int row = this.rowAtPoint(event.getPoint());
        int lpmInd = this.getRowSorter().convertRowIndexToModel(row);
        return this.indexMap.containsKey(lpmInd) ?
                this.indexMap.get(lpmInd).getShortString() : null;
    }
}
