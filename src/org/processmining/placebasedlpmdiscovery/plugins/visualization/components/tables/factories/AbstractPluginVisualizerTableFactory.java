package org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.factories;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.model.TextDescribable;
import org.processmining.placebasedlpmdiscovery.model.serializable.SerializableCollection;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.components.tables.PluginVisualizerTable;

import javax.swing.*;
import java.io.Serializable;

public abstract class AbstractPluginVisualizerTableFactory<T extends TextDescribable & Serializable> {

    public abstract PluginVisualizerTable<T> getPluginVisualizerTable(SerializableCollection<T> result,
                                                                      JComponent visualizerComponent,
                                                                      UIPluginContext context);

}
