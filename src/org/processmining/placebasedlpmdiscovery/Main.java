package org.processmining.placebasedlpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.Analyzer;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.LPMDiscoveryAlgBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.LPMDiscoveryAlgBuilderImpl;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.configurator.FromParametersLPMDAlgBuilderConfigurator;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public class Main {

    public static LPMDiscoveryAlgBuilder createDefaultBuilder(XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryAlgBuilder builder = new LPMDiscoveryAlgBuilderImpl();
        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, builder, runningContext);

        // configure parameters, including set filtration and evaluation controllers
        builder.configureWithConfigurator(new FromParametersLPMDAlgBuilderConfigurator(parameters));
        return builder;
    }

    private static void setupStandardBase(XLog log, LPMDiscoveryAlgBuilder builder, RunningContext runningContext) {
        runningContext.setAnalyzer(new Analyzer(log));
    }

}
