package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables;

import javax.swing.table.DefaultTableModel;
import java.util.Map;
import java.util.function.BiFunction;

public class CustomObjectTableModel<T> extends DefaultTableModel {

    private static final long serialVersionUID = -1387582980490254274L;

    public CustomObjectTableModel(Map<Integer, T> indexMap,
                                  String[] columnNames,
                                  BiFunction<Integer, T, Object[]> objectToColumnsMapper) {
        super(getTableData(indexMap, objectToColumnsMapper), columnNames);
    }

    private static <T> Object[][] getTableData(Map<Integer, T> indexMap, BiFunction<Integer, T, Object[]> objectToColumnsMapper) {
        if (indexMap.size() <= 0)
            return new Object[0][0];
        return indexMap.entrySet()
                .stream()
                .map(entry -> {
                    int ind = entry.getKey();
                    T obj = entry.getValue();
                    return objectToColumnsMapper.apply(ind, obj);
                })
                .toArray(Object[][]::new);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
