package org.processmining.placebasedlpmdiscovery.view.components.general.tables;

import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentListener;

import java.io.Serializable;

public interface TableListener<T extends Serializable> extends ComponentListener {

    void newSelection(T selectedObject);
}
