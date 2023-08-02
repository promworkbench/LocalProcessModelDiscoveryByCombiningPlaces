package org.processmining.placebasedlpmdiscovery.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.analysis.statistics.Statistics;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.plugins.visualization.controllers.InteractiveLPMsDiscoveryController;

import javax.swing.*;

public class InteractiveLPMsDiscovery {

    private PluginContext context;

    private LPMDiscoveryResult result;

    private PlaceBasedLPMDiscoveryParameters parameters;

    public InteractiveLPMsDiscovery(PluginContext context, PlaceBasedLPMDiscoveryParameters parameters, XLog log) {
        this.context = context;

        this.parameters = parameters;

        Main.setUp(context);
        LPMDiscoveryBuilder builder = Main.createDefaultBuilder(log, parameters);
        result = builder.build().run();
    }

    public JComponent getComponentForContext() {
        return new InteractiveLPMsDiscoveryController(result, context).getComponent();
    }
}
