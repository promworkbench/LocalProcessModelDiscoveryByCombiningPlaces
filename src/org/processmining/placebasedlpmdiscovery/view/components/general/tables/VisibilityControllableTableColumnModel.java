package org.processmining.placebasedlpmdiscovery.view.components.general.tables;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableColumn;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class VisibilityControllableTableColumnModel extends DefaultTableColumnModel {
    private static final long serialVersionUID = -4792541007726925693L;
    private final Map<TableColumn, Boolean> columnVisibilityMap;
    protected Vector<TableColumn> allTableColumns;

    public VisibilityControllableTableColumnModel() {
        super();
        this.allTableColumns = new Vector<>();
        this.columnVisibilityMap = new HashMap<>();
    }

    @Override
    public void addColumn(TableColumn aColumn) {
        this.allTableColumns.add(aColumn);
        this.columnVisibilityMap.put(aColumn, true);
        super.addColumn(aColumn);
    }

    @Override
    public void removeColumn(TableColumn column) {
        this.allTableColumns.remove(column);
        this.columnVisibilityMap.remove(column);
        super.removeColumn(column);
    }

    @Override
    public void moveColumn(int columnIndex, int newIndex) {
        if ((columnIndex < 0) || (columnIndex >= getColumnCount()) ||
                (newIndex < 0) || (newIndex >= getColumnCount()))
            throw new IllegalArgumentException("moveColumn() - Index out of range");

        TableColumn fromColumn = tableColumns.get(columnIndex);
        TableColumn toColumn = tableColumns.get(newIndex);

        int allColumnsOldIndex = allTableColumns.indexOf(fromColumn);
        int allColumnsNewIndex = allTableColumns.indexOf(toColumn);

        if (columnIndex != newIndex) {
            allTableColumns.removeElementAt(allColumnsOldIndex);
            allTableColumns.insertElementAt(fromColumn, allColumnsNewIndex);
        }

        super.moveColumn(columnIndex, newIndex);
    }

    public void setColumnVisible(TableColumn column, boolean visible) {
        if (visible == this.columnVisibilityMap.get(column))
            return;

        if (visible)
            super.addColumn(column);
        else
            super.removeColumn(column);
        this.columnVisibilityMap.put(column, visible);
    }

    public void setAllColumnsVisible() {
        for (TableColumn column : this.allTableColumns) {
            this.setColumnVisible(column, true);
        }
    }

    public void keepOnlyFirstColumn() {
        for (int i = 1; i < this.allTableColumns.size(); ++i) {
            this.setColumnVisible(allTableColumns.get(i), false);
        }
    }
}