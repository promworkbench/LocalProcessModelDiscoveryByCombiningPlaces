package org.processmining.placebasedlpmdiscovery.prom.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.lpms.discovery.LPMDiscovery;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algbuilder.LPMDiscoveryAlgBuilder;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.LPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.StandardLPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.parameters.PlaceBasedLPMDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.ContextKeeper;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PetriNetPlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.StandardPlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.parameters.adapters.ParameterAdaptersUtils;
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
            uiLabel = "Basic Local Process Models Discovery"
    )
    @PluginVariant(
            variantLabel = "Basic Local Process Models Discovery",
            requiredParameterLabels = {0}
    )
    public static LPMDiscoveryResult mineBasicLPMs(UIPluginContext context, XLog log) {
        ContextKeeper.setUp(context);

        return LPMDiscovery.getInstance().from(log);
    }

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

        EventLog eventLog = new XLogWrapper(log);
        PlaceBasedLPMDiscoveryPluginParameters parameters = chooseParameters(context, eventLog, true);
        LPMDiscoveryInput input = new StandardLPMDiscoveryInput(eventLog,
                new FPGrowthForPlacesLPMBuildingInput(eventLog, new StandardPlaceDiscovery(log,
                        parameters.getPlaceDiscoveryParameters()).getPlaces().getPlaces()));
        return getLpmDiscoveryResult(input, parameters);
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

        EventLog eventLog = new XLogWrapper(log);
        PlaceBasedLPMDiscoveryPluginParameters parameters = chooseParameters(context, eventLog, false);

        LPMDiscoveryInput input = new StandardLPMDiscoveryInput(eventLog,
                new FPGrowthForPlacesLPMBuildingInput(eventLog,
                        new PetriNetPlaceDiscovery(petrinet).getPlaces().getPlaces()));
        return getLpmDiscoveryResult(input, parameters);
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

        EventLog eventLog = new XLogWrapper(log);
        PlaceBasedLPMDiscoveryPluginParameters parameters = chooseParameters(context, eventLog, false);

        LPMDiscoveryInput input = new StandardLPMDiscoveryInput(eventLog,
                new FPGrowthForPlacesLPMBuildingInput(eventLog, placeSet.getPlaces().getPlaces()));
        return getLpmDiscoveryResult(input, parameters);
    }

    @PluginVariant(
            variantLabel = "Local Process Models Discovery Based on Set of Places given Log",
            requiredParameterLabels = {0}
    )
    public static LPMDiscoveryResult mineLPMs(PluginContext context, XLog log) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryPluginParameters parameters = new PlaceBasedLPMDiscoveryPluginParameters(new XLogWrapper(log));

        return run(context, log, parameters);
    }

    @PluginVariant(
            variantLabel = "Local Process Models Discovery Based on Set of Places given Places (faster)",
            requiredParameterLabels = {0, 1}
    )
    public static LPMDiscoveryResult mineLPMs(PluginContext context, XLog log, PlaceSet placeSet) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryPluginParameters parameters = new PlaceBasedLPMDiscoveryPluginParameters(new XLogWrapper(log));
        return run(context, log, placeSet, parameters);
    }

    @PluginVariant(
            variantLabel = "Local Process Models Discovery Based on Set of Places given Petri Net (faster)",
            requiredParameterLabels = {0, 2}
    )
    public static LPMDiscoveryResult mineLPMs(PluginContext context, XLog log, Petrinet petrinet) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryPluginParameters parameters = new PlaceBasedLPMDiscoveryPluginParameters(new XLogWrapper(log));
        PlaceSet places = new PlaceSet(PlaceUtils.getPlacesFromPetriNet(petrinet));

        return run(context, log, places, parameters);
    }

    @PluginVariant(
            variantLabel = "Local Process Models Discovery Based on Set of Places given Log",
            requiredParameterLabels = {0, 3}
    )
    public static LPMDiscoveryResult mineLPMs(PluginContext context, XLog log, PlaceBasedLPMDiscoveryPluginParameters parameters) {
        return run(context, log, parameters);
    }

    @PluginVariant(
            variantLabel = "Local Process Models Discovery Based on Set of Places given Places (faster)",
            requiredParameterLabels = {0, 1, 3}
    )
    public static LPMDiscoveryResult mineLPMs(PluginContext context, XLog log, PlaceSet placeSet, PlaceBasedLPMDiscoveryPluginParameters parameters) {
        return run(context, log, placeSet, parameters);
    }

    private static LPMDiscoveryResult run(PluginContext context, XLog log, PlaceBasedLPMDiscoveryPluginParameters parameters) {
        ContextKeeper.setUp(context);

        EventLog eventLog = new XLogWrapper(log);
        LPMDiscoveryInput input = new StandardLPMDiscoveryInput(eventLog,
                new FPGrowthForPlacesLPMBuildingInput(eventLog, new StandardPlaceDiscovery(log,
                        parameters.getPlaceDiscoveryParameters()).getPlaces().getPlaces()));
        return getLpmDiscoveryResult(input, parameters);
    }

    private static LPMDiscoveryResult run(PluginContext context, XLog log, PlaceSet placeSet, PlaceBasedLPMDiscoveryPluginParameters parameters) {
        ContextKeeper.setUp(context);

        EventLog eventLog = new XLogWrapper(log);
        LPMDiscoveryInput input = new StandardLPMDiscoveryInput(eventLog,
                new FPGrowthForPlacesLPMBuildingInput(eventLog, placeSet.getPlaces().getPlaces()));
        return getLpmDiscoveryResult(input, parameters);
    }

    public static LPMDiscoveryResult getLpmDiscoveryResult(LPMDiscoveryInput input,
                                                            PlaceBasedLPMDiscoveryPluginParameters parameters) {
        if (parameters == null)
            return null;

        PlaceBasedLPMDiscoveryParameters algParam = ParameterAdaptersUtils.transform(parameters, input.getLog());
        LPMDiscoveryAlgBuilder builder = Main.createDefaultBuilder(input.getLog().getOriginalLog(), algParam);

        return builder.build().run(input, algParam);
    }

    private static PlaceBasedLPMDiscoveryPluginParameters chooseParameters(UIPluginContext context, EventLog eventLog, boolean placeDiscoveryIncluded) {
        PlaceBasedLPMDiscoveryPluginParameters parameters =
                new PlaceBasedLPMDiscoveryPluginParameters(eventLog);

        // show wizard
        Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryPluginParameters>> stepMap = new HashMap<>();
        if (placeDiscoveryIncluded) {
            stepMap.put(PlaceBasedLPMDiscoveryWizard.INITIAL_KEY, new PlaceDiscoveryAlgorithmChoiceWizardStep());
            stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_EST_MINER, new ESTMinerWizardStep(eventLog.getOriginalLog()));
            stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_INDUCTIVE_MINER, new InductiveMinerWizardStep(eventLog.getOriginalLog()));
            stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_HEURISTIC_MINER, new HeuristicMinerWizardStep(eventLog.getOriginalLog()));
        }
        stepMap.put(PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY, new LPMDiscoveryWizardStep(eventLog.getOriginalLog()));
//		stepMap.put(PlaceBasedLPMDiscoveryWizard.EVALUATION, new EvaluationWizardStep());
        PlaceBasedLPMDiscoveryWizard wizard = new PlaceBasedLPMDiscoveryWizard(stepMap, placeDiscoveryIncluded);
        return ProMWizardDisplay.show(context, wizard, parameters);
    }

}
