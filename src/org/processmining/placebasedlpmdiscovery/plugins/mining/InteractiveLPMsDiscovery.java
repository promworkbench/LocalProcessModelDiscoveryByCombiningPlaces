package org.processmining.placebasedlpmdiscovery.plugins.mining;

import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers.InteractiveLPMsDiscoveryController;

import javax.swing.*;

public class InteractiveLPMsDiscovery {

    private LPMResult lpmResult;

    private PlaceBasedLPMDiscoveryParameters parameters;

    public InteractiveLPMsDiscovery(PlaceBasedLPMDiscoveryParameters parameters) {
        this.parameters = parameters;
    }

    public JComponent getComponentForContext(final UIPluginContext context) {
        return new InteractiveLPMsDiscoveryController(context, this).getComponent();
    }

    public LPMResult getResult() {
        return lpmResult;
    }
}
