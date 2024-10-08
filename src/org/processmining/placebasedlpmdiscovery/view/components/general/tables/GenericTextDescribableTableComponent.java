package org.processmining.placebasedlpmdiscovery.view.components.general.tables;

import org.processmining.placebasedlpmdiscovery.model.TextDescribable;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.UUID;

public class GenericTextDescribableTableComponent<T extends TextDescribable> extends JTable {

    private final Map<Integer, T> indexMap;
    private final UUID id;

    public GenericTextDescribableTableComponent(Map<Integer, T> indexMap) {
        this.id = UUID.randomUUID();
        this.indexMap = indexMap;
    }

    public Map<Integer, T> getIndexMap() {
        return indexMap;
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
        int ind = this.getRowSorter().convertRowIndexToModel(row);
        return this.indexMap.containsKey(ind) ?
                this.indexMap.get(ind).getShortString() : null;
    }

    public String getId() {
        return this.id.toString();
    }
}
