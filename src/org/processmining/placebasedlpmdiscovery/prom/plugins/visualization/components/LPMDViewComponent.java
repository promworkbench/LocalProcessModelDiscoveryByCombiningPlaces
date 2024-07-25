package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

import org.processmining.placebasedlpmdiscovery.view.model.lpmdistances.ModelDistanceVM;

import java.awt.*;

public interface LPMDViewComponent {
    Component getComponent();

    ModelDistanceVM getModel();
}
