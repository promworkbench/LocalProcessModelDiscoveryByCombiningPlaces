package org.processmining.placebasedlpmdiscovery.view.components.placesetdisplay;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.CustomObjectTableModel;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.GenericTextDescribableTableComponent;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.factories.AbstractPluginVisualizerTableFactory;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.EmittableDataTypeVM;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.emittabledata.export.ExportRequestedEmittableDataVM;
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
            this.dcVM.emit(new ExportRequestedEmittableDataVM(res));
        });
        popupMenu.add(exportItem);
        return popupMenu;
    }

    @Override
    protected void onNewSelection(Place selectedObject, String tableId) {
        this.dcVM.emit(new NewPlaceSelectedEmittableDataVM(
                String.join("/", EmittableDataTypeVM.NewPlaceSelectedVM.name(), tableId),
                selectedObject));
    }

}
