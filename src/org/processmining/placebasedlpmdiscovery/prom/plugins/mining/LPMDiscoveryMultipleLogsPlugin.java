package org.processmining.placebasedlpmdiscovery.prom.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.lpmbuilding.inputs.FPGrowthForPlacesLPMBuildingInput;
import org.processmining.placebasedlpmdiscovery.lpmdiscovery.algorithms.inputs.StandardLPMDiscoveryInput;
import org.processmining.placebasedlpmdiscovery.main.LPMDiscoveryBuilder;
import org.processmining.placebasedlpmdiscovery.model.discovery.LPMDiscoveryResult;
import org.processmining.placebasedlpmdiscovery.main.MultipleLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.model.inout.TwoStandardLPMDiscoveryResults;
import org.processmining.placebasedlpmdiscovery.model.logs.EventLog;
import org.processmining.placebasedlpmdiscovery.model.logs.XLogWrapper;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.prom.ContextKeeper;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PetriNetPlaceDiscovery;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.wizards.PlaceBasedLPMDiscoveryWizard;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.wizards.steps.LPMDiscoveryWizardStep;

import java.util.HashMap;
import java.util.Map;

@Plugin(
        name = "Local Process Models Discovery Based on Multiple Event Logs",
        parameterLabels = {"Log1", "Log2", "Set of Places", "Petri Net", "Parameters"},
        returnLabels = {"Multiple LPM sets"},
        returnTypes = {MultipleLPMDiscoveryResults.class},
        help = "Finds Local Process Models on multiple logs"
)
public class LPMDiscoveryMultipleLogsPlugin {

    @UITopiaVariant(
            affiliation = "RWTH - PADS",
            author = "Viki Peeva",
            email = "peeve@pads.rwth-aachen.de",
            uiLabel = "Local Process Models Discovery on Two Event Logs"
    )
    @PluginVariant(
            variantLabel = "Local Process Models Discovery on Two Event Logs",
            requiredParameterLabels = {0, 1, 3}
    )
    public static MultipleLPMDiscoveryResults mineLPMs(UIPluginContext context, XLog log1, XLog log2, Petrinet petrinet) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryParameters parameters = getPlaceBasedLPMDiscoveryParameters(context, log1);
        if (parameters == null) return null;

        EventLog elog1 = new XLogWrapper(log1);
        LPMDiscoveryBuilder builder1 = Main.createDefaultForPetriNetBuilder(log1, petrinet, parameters);
        LPMDiscoveryResult res1 = builder1.build().run(new StandardLPMDiscoveryInput(elog1,
                new FPGrowthForPlacesLPMBuildingInput(elog1,
                        new PetriNetPlaceDiscovery(petrinet).getPlaces().getPlaces())));

        EventLog elog2 = new XLogWrapper(log2);
        LPMDiscoveryBuilder builder2 = Main.createDefaultForPetriNetBuilder(log2, petrinet, parameters);
        LPMDiscoveryResult res2 = builder2.build().run(new StandardLPMDiscoveryInput(elog2,
                new FPGrowthForPlacesLPMBuildingInput(elog2,
                        new PetriNetPlaceDiscovery(petrinet).getPlaces().getPlaces())));

        return new TwoStandardLPMDiscoveryResults(res1, res2);
    }

    @UITopiaVariant(
            affiliation = "RWTH - PADS",
            author = "Viki Peeva",
            email = "peeve@pads.rwth-aachen.de",
            uiLabel = "Local Process Models Discovery on Two Event Logs"
    )
    @PluginVariant(
            variantLabel = "Local Process Models Discovery on Two Event Logs",
            requiredParameterLabels = {0, 1, 3}
    )
    public static MultipleLPMDiscoveryResults mineLPMs(UIPluginContext context, XLog log1, XLog log2, PlaceSet placeSet) {
        ContextKeeper.setUp(context);

        PlaceBasedLPMDiscoveryParameters parameters = getPlaceBasedLPMDiscoveryParameters(context, log1);
        if (parameters == null) return null;

        EventLog elog1 = new XLogWrapper(log1);
        LPMDiscoveryBuilder builder1 = Main.createDefaultBuilder(log1, placeSet, parameters);
        LPMDiscoveryResult res1 = builder1.build().run(new StandardLPMDiscoveryInput(elog1,
                new FPGrowthForPlacesLPMBuildingInput(elog1, placeSet.getPlaces().getPlaces())));

        EventLog elog2 = new XLogWrapper(log2);
        LPMDiscoveryBuilder builder2 = Main.createDefaultBuilder(log2, placeSet, parameters);
        LPMDiscoveryResult res2 = builder2.build().run(new StandardLPMDiscoveryInput(elog2,
                new FPGrowthForPlacesLPMBuildingInput(elog2, placeSet.getPlaces().getPlaces())));

        return new TwoStandardLPMDiscoveryResults(res1, res2);
    }

    private static PlaceBasedLPMDiscoveryParameters getPlaceBasedLPMDiscoveryParameters(UIPluginContext context, XLog log1) {
        PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(new XLogWrapper(log1));

        // show wizard
        Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryParameters>> stepMap = new HashMap<>();
        stepMap.put(PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY, new LPMDiscoveryWizardStep(log1));
        PlaceBasedLPMDiscoveryWizard wizard = new PlaceBasedLPMDiscoveryWizard(stepMap, false);
        parameters = ProMWizardDisplay.show(context, wizard, parameters);
        return parameters;
    }
}
