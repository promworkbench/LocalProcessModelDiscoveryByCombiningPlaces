package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentId;

import javax.swing.*;

public interface SettablePanelFactory {

    JPanel getSettablePanel(ComponentId.Type type);
}
