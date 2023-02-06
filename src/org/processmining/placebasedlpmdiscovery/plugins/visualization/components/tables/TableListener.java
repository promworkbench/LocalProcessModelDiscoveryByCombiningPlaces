package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables;

import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.ComponentListener;

import java.io.Serializable;

public interface TableListener<T extends Serializable> extends ComponentListener {

    void newSelection(T selectedObject);

    void export(SerializableCollection<T> collection);
}
