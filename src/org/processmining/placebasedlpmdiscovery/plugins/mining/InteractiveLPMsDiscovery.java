package org.processmining.placebasedlpmdiscovery.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers.InteractiveLPMsDiscoveryController;

import javax.swing.*;

public class InteractiveLPMsDiscovery {

    private PluginContext context;

    private LPMResult lpmResult;

    private PlaceBasedLPMDiscoveryParameters parameters;

    public InteractiveLPMsDiscovery(PluginContext context, PlaceBasedLPMDiscoveryParameters parameters, XLog log) {
        this.context = context;

        this.parameters = parameters;

        Main.setUp(context);
        this.lpmResult = (LPMResult) Main.run(log, parameters)[0];
    }

    public JComponent getComponentForContext() {
        return new InteractiveLPMsDiscoveryController(lpmResult, context).getComponent();
    }

    public LPMResult getResult() {
        return lpmResult;
    }
}
