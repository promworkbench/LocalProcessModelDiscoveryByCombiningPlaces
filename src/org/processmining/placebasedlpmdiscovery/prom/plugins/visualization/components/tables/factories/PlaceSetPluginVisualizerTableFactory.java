package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.CustomObjectTableModel;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.GenericTextDescribableTableComponent;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.tableselection.NewPlaceSelectedEmittableDataVM;

import javax.swing.*;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PlaceSetPluginVisualizerTableFactory extends AbstractPluginVisualizerTableFactory<Place> {

    private final DataCommunicationControllerVM dcVM;

    @Inject
    public PlaceSetPluginVisualizerTableFactory(DataCommunicationControllerVM dcVM) {
        this.dcVM = dcVM;
    }

    @Override
    protected Map<Integer, Place> getIndexObjectMap(Collection<Place> elements) {
        final Iterator<Place> placeIterator = elements.iterator();
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
    protected JPopupMenu getPopupMenu(GenericTextDescribableTableComponent<Place> table) {
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

    @Override
    protected void newSelection(Place selectedObject) {
        this.dcVM.emit(new NewPlaceSelectedEmittableDataVM(selectedObject));
    }

}
