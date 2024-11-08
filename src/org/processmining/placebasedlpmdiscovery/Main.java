package org.processmining.placebasedlpmdiscovery;

import org.deckfour.xes.model.XLog;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.placebasedlpmdiscovery.analysis.analyzers.Analyzer;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.directors.FromParametersLPMDiscoveryDirector;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.main.StandardLPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

public class Main {

    public static LPMDiscoveryBuilder createDefaultBuilder(XLog log, PlaceSet placeSet,
                                                           PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryBuilder builder = new StandardLPMDiscoveryBuilder();

        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, builder, runningContext);


        // set filtration and evaluation controllers
        setupStandardEvaluationAndFiltrationControllers(parameters, builder);

        return builder;
    }

    public static LPMDiscoveryBuilder createDefaultBuilder(XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryBuilder builder = new StandardLPMDiscoveryBuilder();

        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, builder, runningContext);

        // set filtration and evaluation controllers
        setupStandardEvaluationAndFiltrationControllers(parameters, builder);
        return builder;
    }

    public static LPMDiscoveryBuilder createDefaultForPetriNetBuilder(XLog log, Petrinet petrinet,
                                                                      PlaceBasedLPMDiscoveryParameters parameters) {
        // create builder
        LPMDiscoveryBuilder builder = new StandardLPMDiscoveryBuilder();

        // set running context
        RunningContext runningContext = new RunningContext();
        setupStandardBase(log, builder, runningContext);

        // set filtration and evaluation controllers
        setupStandardEvaluationAndFiltrationControllers(parameters, builder);
        return builder;
    }

    private static void setupStandardBase(XLog log, LPMDiscoveryBuilder builder, RunningContext runningContext) {
        runningContext.setAnalyzer(new Analyzer(log));
        builder.setRunningContext(runningContext);
    }

    private static void setupStandardEvaluationAndFiltrationControllers(PlaceBasedLPMDiscoveryParameters parameters,
                                                                        LPMDiscoveryBuilder builder) {
        FromParametersLPMDiscoveryDirector director = new FromParametersLPMDiscoveryDirector(builder, parameters);
        director.make();
    }
}
