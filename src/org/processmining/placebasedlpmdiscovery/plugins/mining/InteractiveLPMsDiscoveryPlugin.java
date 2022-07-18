package org.processmining.placebasedlpmdiscovery.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.PlaceBasedLPMDiscoveryWizard;
import org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.steps.*;

import java.util.HashMap;
import java.util.Map;

@Plugin(
        name = "Interactive Local Process Models Discovery",
        parameterLabels = {"Log", "Set of Places", "Petri Net", "Parameters"},
        returnLabels = {"Interactive LPM Discovery"},
        returnTypes = {InteractiveLPMsDiscovery.class},
        help = "Discover Local Process Models"
)
public class InteractiveLPMsDiscoveryPlugin {

    @UITopiaVariant(
            affiliation = "RWTH - PADS",
            author = "Viki Peeva",
            email = "peeva@pads.rwth-aachen.de",
            uiLabel = "Interactive Local Process Models Discovery"
    )
    @PluginVariant(
            variantLabel = "Interactive Local Process Models Discovery",
            requiredParameterLabels = {0}
    )
    public static InteractiveLPMsDiscovery mineInteractive(UIPluginContext context, XLog log){
        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);

        // show wizard
        Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryParameters>> stepMap = new HashMap<>();
        stepMap.put(PlaceBasedLPMDiscoveryWizard.INITIAL_KEY, new PlaceDiscoveryAlgorithmChoiceWizardStep());
        stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_EST_MINER, new ESTMinerWizardStep(log));
        stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_INDUCTIVE_MINER, new InductiveMinerWizardStep(log));
        stepMap.put(PlaceBasedLPMDiscoveryWizard.PD_HEURISTIC_MINER, new HeuristicMinerWizardStep(log));
        stepMap.put(PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY, new LPMDiscoveryWizardStep(log));
        PlaceBasedLPMDiscoveryWizard wizard = new PlaceBasedLPMDiscoveryWizard(stepMap, true);
        parameters = ProMWizardDisplay.show(context, wizard, parameters);

        if (parameters == null) {
            context.getTask().pluginCancelled(context);
            return null;
        }

        return new InteractiveLPMsDiscovery(parameters);
    }
}
