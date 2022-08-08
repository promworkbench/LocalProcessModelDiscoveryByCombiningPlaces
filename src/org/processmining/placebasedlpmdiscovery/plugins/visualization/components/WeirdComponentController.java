package org.processmining.placebasedlpmdiscovery.plugins.visualization.components;

import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.TableListener;

public interface WeirdComponentController<T> extends TableListener<T> {

    void componentExpansion(ComponentId componentId, boolean expanded);
}
