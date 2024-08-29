package org.processmining.placebasedlpmdiscovery.prom.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.ContextKeeper;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.wizards.PlaceBasedLPMDiscoveryWizard;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.wizards.steps.*;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.HashMap;
import java.util.Map;


@Plugin(
        name = "Local Process Models Discovery",
        parameterLabels = {"Log", "Set of Places", "Petri Net", "Parameters"},
        returnLabels = {"LPM set"},
        returnTypes = {LPMDiscoveryResult.class},
        help = "Builds Local Process Models"
)
public class LPMDiscoveryPlugin {
    @UITopiaVariant(
            affiliation = "RWTH - PADS",
            author = "Viki Peeva",
            email = "viki.peeva@rwth-aachen.de",
            uiLabel = "Local Process Models Discovery"
    )
    @PluginVariant(
            variantLabel = "Local Process Models Discovery",
            requiredParameterLabels = {0}
    )
    public static LPMDiscoveryResult mineLPMs(UIPluginContext context, XLog log) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log));

        // show wizard
        Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryParameters>> stepMap = new HashMap<>();
        stepMap.put(PlaceBasedLPMDiscoveryWizard.INITIAL_KEY, new PlaceDiscoveryAlgorithmChoiceWizardStep());
        stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_EST_MINER, new ESTMinerWizardStep(log));
        stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_INDUCTIVE_MINER, new InductiveMinerWizardStep(log));
        stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_HEURISTIC_MINER, new HeuristicMinerWizardStep(log));
        stepMap.put(PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY, new LPMDiscoveryWizardStep(log));
//		stepMap.put(PlaceBasedLPMDiscoveryWizard.EVALUATION, new EvaluationWizardStep());
        PlaceBasedLPMDiscoveryWizard wizard = new PlaceBasedLPMDiscoveryWizard(stepMap, true);
        parameters = ProMWizardDisplay.show(context, wizard, parameters);

        if (parameters == null)
            return null;

        LPMDiscoveryBuilder builder = Main.createDefaultBuilder(log, parameters);
        return builder.build().run();
    }

    @UITopiaVariant(
            affiliation = "RWTH - PADS",
            author = "Viki Peeva",
            email = "viki.peeva@rwth-aachen.de",
            uiLabel = "Local Process Models Discovery given Petri Net (faster)"
    )
    @PluginVariant(
            variantLabel = "Local Process Models Discovery given Petri Net (faster)",
            requiredParameterLabels = {0, 2}
    )
    public static LPMDiscoveryResult mineLPMs(UIPluginContext context, XLog log, Petrinet petrinet) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log));

        // show wizard
        Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryParameters>> stepMap = new HashMap<>();
        stepMap.put(PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY, new LPMDiscoveryWizardStep(log));
//		stepMap.put(PlaceBasedLPMDiscoveryWizard.EVALUATION, new EvaluationWizardStep());
        PlaceBasedLPMDiscoveryWizard wizard = new PlaceBasedLPMDiscoveryWizard(stepMap, false);
        parameters = ProMWizardDisplay.show(context, wizard, parameters);

        if (parameters == null)
            return null;

        LPMDiscoveryBuilder builder = Main.createDefaultForPetriNetBuilder(log, petrinet, parameters);
        return builder.build().run();
    }

    @UITopiaVariant(
            affiliation = "RWTH - PADS",
            author = "Viki Peeva",
            email = "viki.peeva@rwth-aachen.de",
            uiLabel = "Local Process Models Discovery given Places (faster)"
    )
    @PluginVariant(
            variantLabel = "Local Process Models Discovery given Places (faster)",
            requiredParameterLabels = {0, 1}
    )
    public static LPMDiscoveryResult mineLPMs(UIPluginContext context, XLog log, PlaceSet placeSet) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log));

        // show wizard
        Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryParameters>> stepMap = new HashMap<>();
        stepMap.put(PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY, new LPMDiscoveryWizardStep(log));
//		stepMap.put(PlaceBasedLPMDiscoveryWizard.EVALUATION, new EvaluationWizardStep());
        PlaceBasedLPMDiscoveryWizard wizard = new PlaceBasedLPMDiscoveryWizard(stepMap, false);
        parameters = ProMWizardDisplay.show(context, wizard, parameters);

        if (parameters == null)
            return null;

        LPMDiscoveryBuilder builder = Main.createDefaultBuilder(log, placeSet, parameters);
        return builder.build().run();
    }

    @PluginVariant(
            variantLabel = "Local Process Models Discovery Based on Set of Places given Log",
            requiredParameterLabels = {0}
    )
    public static LPMDiscoveryResult mineLPMs(PluginContext context, XLog log) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log));

        LPMDiscoveryBuilder builder = Main.createDefaultBuilder(log, parameters);
        return builder.build().run();
    }

    @PluginVariant(
            variantLabel = "Local Process Models Discovery Based on Set of Places given Places (faster)",
            requiredParameterLabels = {0, 1}
    )
    public static LPMDiscoveryResult mineLPMs(PluginContext context, XLog log, PlaceSet placeSet) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log));

        LPMDiscoveryBuilder builder = Main.createDefaultBuilder(log, placeSet, parameters);
        return builder.build().run();
    }

    @PluginVariant(
            variantLabel = "Local Process Models Discovery Based on Set of Places given Petri Net (faster)",
            requiredParameterLabels = {0, 2}
    )
    public static LPMDiscoveryResult mineLPMs(PluginContext context, XLog log, Petrinet petrinet) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log));
        PlaceSet places = new PlaceSet(PlaceUtils.getPlacesFromPetriNet(petrinet));

        return run(context, log, places, parameters);
    }

    @PluginVariant(
            variantLabel = "Local Process Models Discovery Based on Set of Places given Log",
            requiredParameterLabels = {0, 3}
    )
    public static LPMDiscoveryResult mineLPMs(PluginContext context, XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        return run(context, log, parameters);
    }

    @PluginVariant(
            variantLabel = "Local Process Models Discovery Based on Set of Places given Places (faster)",
            requiredParameterLabels = {0, 1, 3}
    )
    public static LPMDiscoveryResult mineLPMs(PluginContext context, XLog log, PlaceSet placeSet, PlaceBasedLPMDiscoveryParameters parameters) {
        return run(context, log, placeSet, parameters);
    }

    private static LPMDiscoveryResult run(PluginContext context, XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
        ContextKeeper.setUp(context);
        LPMDiscoveryBuilder builder = Main.createDefaultBuilder(log, parameters);
        return builder.build().run();
    }

    private static LPMDiscoveryResult run(PluginContext context, XLog log, PlaceSet placeSet, PlaceBasedLPMDiscoveryParameters parameters) {
        ContextKeeper.setUp(context);
        LPMDiscoveryBuilder builder = Main.createDefaultBuilder(log, placeSet, parameters);
        return builder.build().run();
    }


}
