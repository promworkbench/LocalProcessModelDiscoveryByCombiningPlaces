package org.processmining.placebasedlpmdiscovery.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.PlaceBasedLPMDiscoveryWizard;
import org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.steps.*;

import java.util.HashMap;
import java.util.Map;

@Plugin(
        name = "UC Local Process Models Discovery Based on Set of Places",
        parameterLabels = {"Log", "Set of Places", "Petri Net", "Parameters"},
        returnLabels = {"LPM set"},
        returnTypes = {LPMResult.class},
        help = "Finds Local Process Models for a given set of places"
)
public class UCLPMDiscoveryPlugin {
    @UITopiaVariant(
            affiliation = "RWTH - PADS",
            author = "Viki Peeva",
            email = "peeva@pads.rwth-aachen.de",
            uiLabel = "Context Local Process Models Discovery Based on Set of Place Nets given Event Log"
    )
    @PluginVariant(
            variantLabel = "Context Local Process Models Discovery Based on Set of Place Nets given Event Log",
            requiredParameterLabels = {0}
    )
    public static LPMResult mineLPMs(UIPluginContext context, XLog log) {
        Main.setUp(context);

        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);

        // show wizard
        Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryParameters>> stepMap = new HashMap<>();
//        stepMap.put(PlaceBasedLPMDiscoveryWizard.INITIAL_KEY, new PlaceDiscoveryAlgorithmChoiceWizardStep());
//        stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_EST_MINER, new ESTMinerWizardStep(log));
//        stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_INDUCTIVE_MINER, new InductiveMinerWizardStep(log));
//        stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_HEURISTIC_MINER, new HeuristicMinerWizardStep(log));
//        stepMap.put(PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY, new LPMDiscoveryWizardStep(log));
        stepMap.put(PlaceBasedLPMDiscoveryWizard.LPM_CONTEXT, new LPMContextWizardStep(log));
        PlaceBasedLPMDiscoveryWizard wizard = new PlaceBasedLPMDiscoveryWizard(stepMap, false, true);
        parameters = ProMWizardDisplay.show(context, wizard, parameters);

        if (parameters == null)
            return null;

        return (LPMResult) Main.run(log, parameters)[0];
    }
}
