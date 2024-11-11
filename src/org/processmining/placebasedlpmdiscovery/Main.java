package org.processmining.placebasedlpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.Analyzer;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.directors.FromParametersLPMDiscoveryDirector;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.LPMDiscoveryAlgBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.StandardLPMDiscoveryAlgBuilder;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public class Main {

    public static LPMDiscoveryAlgBuilder createDefaultBuilder(XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryAlgBuilder builder = new StandardLPMDiscoveryAlgBuilder();
        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, builder, runningContext);
        // set filtration and evaluation controllers
        setupStandardEvaluationAndFiltrationControllers(parameters, builder);
        return builder;
    }

    private static void setupStandardBase(XLog log, LPMDiscoveryAlgBuilder builder, RunningContext runningContext) {
        runningContext.setAnalyzer(new Analyzer(log));
        builder.setRunningContext(runningContext);
    }

    private static void setupStandardEvaluationAndFiltrationControllers(PlaceBasedLPMDiscoveryParameters parameters,
                                                                        LPMDiscoveryAlgBuilder builder) {
        FromParametersLPMDiscoveryDirector director = new FromParametersLPMDiscoveryDirector(builder, parameters);
        director.make();
    }
}
