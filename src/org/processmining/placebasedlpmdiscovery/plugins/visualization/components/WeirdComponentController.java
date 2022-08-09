package org.processmining.placebasedlpmdiscovery.plugins.visualization.components;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.TableListener;

public interface WeirdComponentController<T> extends TableListener<T>, ComponentListener {

    void placeSelected(Place p);

    void transitionSelected(Transition t);
}
