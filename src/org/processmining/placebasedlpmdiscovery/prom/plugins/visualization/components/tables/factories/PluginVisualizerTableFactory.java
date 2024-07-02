package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.factories;

import org.processmining.placebasedlpmdiscovery.model.TextDescribable;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.GenericTextDescribableTableComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.tables.TableListener;

import java.io.Serializable;
import java.util.Collection;

public interface PluginVisualizerTableFactory<T extends TextDescribable & Serializable> {

    GenericTextDescribableTableComponent<T> getPluginVisualizerTable(Collection<T> result, TableListener<T> listener);
}
