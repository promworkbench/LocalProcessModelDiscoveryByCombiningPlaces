package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.Transition;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.TableListener;

import java.io.Serializable;

public interface WeirdComponentController<T extends Serializable> extends TableListener<T>, ComponentListener {

    void placeSelected(Place p);

    void transitionSelected(Transition t);
}
