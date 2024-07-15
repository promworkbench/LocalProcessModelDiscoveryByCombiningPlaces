package org.processmining.placebasedlpmdiscovery.view.components.general.tables.factories;

import org.processmining.placebasedlpmdiscovery.model.TextDescribable;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.TableListener;
import org.processmining.placebasedlpmdiscovery.view.components.general.tables.GenericTextDescribableTableComponent;

import java.io.Serializable;
import java.util.Collection;

public interface PluginVisualizerTableFactory<T extends TextDescribable & Serializable> {

    GenericTextDescribableTableComponent<T> getPluginVisualizerTable(Collection<T> result, TableListener<T> listener);
}
