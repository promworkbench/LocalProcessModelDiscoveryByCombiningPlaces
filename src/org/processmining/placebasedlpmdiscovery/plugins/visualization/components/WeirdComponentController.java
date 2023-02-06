package org.processmining.placebasedlpmdiscovery.plugins.visualization.components;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.TableListener;

import java.io.Serializable;

public interface WeirdComponentController<T extends Serializable> extends TableListener<T>, ComponentListener {

    void placeSelected(Place p);

    void transitionSelected(Transition t);
}
