package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components;

public interface ComponentFactory {
    LPMDViewComponent create(LPMDViewComponentType lpmdViewComponentType);
}
