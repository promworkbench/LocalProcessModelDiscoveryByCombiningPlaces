package org.processmining.placebasedlpmdiscovery.prom.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.controllers.InteractiveLPMsDiscoveryController;
import org.processmining.placebasedlpmdiscovery.prom.ContextKeeper;

import javax.swing.*;

public class InteractiveLPMsDiscovery {

    private PluginContext context;

    private LPMDiscoveryResult result;

    private PlaceBasedLPMDiscoveryParameters parameters;

    public InteractiveLPMsDiscovery(PluginContext context, PlaceBasedLPMDiscoveryParameters parameters, XLog log) {
        this.context = context;

        this.parameters = parameters;

        ContextKeeper.setUp(context);
        LPMDiscoveryBuilder builder = Main.createDefaultBuilder(log, parameters);
        result = builder.build().run();
    }

    public JComponent getComponentForContext() {
        return new InteractiveLPMsDiscoveryController(result, context).getComponent();
    }
}
