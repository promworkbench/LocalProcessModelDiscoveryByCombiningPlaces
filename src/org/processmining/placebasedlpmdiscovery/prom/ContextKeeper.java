package org.processmining.placebasedlpmdiscovery.prom;

import org.processmining.framework.plugin.PluginContext;

public class ContextKeeper {

    private static PluginContext Context;

    public static void setUp(PluginContext context) {
        ContextKeeper.Context = context;
    }

    public static PluginContext getContext() {
        return Context;
    }
}
