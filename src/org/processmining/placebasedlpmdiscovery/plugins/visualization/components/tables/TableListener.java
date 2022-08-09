package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables;

import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.ComponentListener;

public interface TableListener<T> extends ComponentListener {

    void newSelection(T selectedObject);
}
