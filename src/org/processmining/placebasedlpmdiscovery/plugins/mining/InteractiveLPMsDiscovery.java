package org.processmining.placebasedlpmdiscovery.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIContext;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers.InteractiveLPMsDiscoveryController;

import javax.swing.*;

public class InteractiveLPMsDiscovery {

    private LPMResult lpmResult;

    private PlaceBasedLPMDiscoveryParameters parameters;

    public InteractiveLPMsDiscovery(UIPluginContext context, PlaceBasedLPMDiscoveryParameters parameters, XLog log) {
        this.parameters = parameters;

        Main.setUp(context);
        this.lpmResult = (LPMResult) Main.run(log, parameters)[0];
    }

    public JComponent getComponentForContext(final UIPluginContext context) {
        return new InteractiveLPMsDiscoveryController(context, this).getComponent();
    }

    public LPMResult getResult() {
        return lpmResult;
    }
}
