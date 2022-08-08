package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.CustomObjectTableModel;

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

}
