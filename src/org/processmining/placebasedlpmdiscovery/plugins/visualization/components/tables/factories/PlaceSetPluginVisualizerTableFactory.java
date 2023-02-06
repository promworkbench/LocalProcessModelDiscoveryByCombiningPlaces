package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.CustomObjectTableModel;

import javax.swing.*;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlaceSetPluginVisualizerTableFactory extends AbstractPluginVisualizerTableFactory<Place> {

    @Override
    protected Map<Integer, Place> getIndexObjectMap(SerializableCollection<Place> elements) {
        final Iterator<Place> placeIterator = elements.getElements().iterator();
        return IntStream
                .range(0, elements.size())
                .boxed()
                .collect(Collectors.toMap(i -> i, i -> placeIterator.next()));
    }

    @Override
    protected CustomObjectTableModel<Place> createTableModel(Map<Integer, Place> indexObjectMap) {
        return new CustomObjectTableModel<>(
                indexObjectMap,
                new String[]{"Place Index", "Place Short Name"},
                (ind, place) -> new Object[]{
                        ind + 1,
                        place.getShortString()
                });
    }

    @Override
    protected JPopupMenu getPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem exportItem = new JMenuItem("Export");
        exportItem.addActionListener(e -> {
            PlaceSet res = new PlaceSet();
            for (Integer ind : table.getSelectedRows()) {
                res.add(table.getIndexMap().get(table.convertRowIndexToModel(ind)));
            }
            this.listener.export(res);
        });
        popupMenu.add(exportItem);
        return popupMenu;
    }

}
