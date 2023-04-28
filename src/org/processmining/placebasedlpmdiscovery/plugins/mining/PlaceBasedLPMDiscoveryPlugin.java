package org.processmining.placebasedlpmdiscovery.plugins.mining;

import org.deckfour.xes.model.XLog;
import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.acceptingpetrinet.models.impl.AcceptingPetriNetImpl;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.connections.ConnectionCannotBeObtained;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.framework.util.ui.wizard.ProMWizardDisplay;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.placebasedlpmdiscovery.Main;
import org.processmining.placebasedlpmdiscovery.model.Place;
import org.processmining.placebasedlpmdiscovery.model.serializable.LPMResult;
import org.processmining.placebasedlpmdiscovery.model.serializable.PlaceSet;
import org.processmining.placebasedlpmdiscovery.placediscovery.converters.place.PetriNetPlaceConverter;
import org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.PlaceBasedLPMDiscoveryWizard;
import org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.steps.*;
import org.processmining.models.connections.petrinets.behavioral.FinalMarkingConnection;
import org.processmining.models.connections.petrinets.behavioral.InitialMarkingConnection;
import org.processmining.models.graphbased.directed.petrinet.Petrinet;
import org.processmining.models.semantics.petrinet.Marking;
import org.processmining.placebasedlpmdiscovery.utils.PlaceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Plugin(
		name = "Local Process Models Discovery Based on Set of Places",
		parameterLabels = {"Log", "Set of Places", "Petri Net", "Parameters"},
		returnLabels = {"LPM set"},
		returnTypes = {LPMResult.class},
		help = "Finds Local Process Models for a given set of places"
)
public class PlaceBasedLPMDiscoveryPlugin {
	@UITopiaVariant(
			affiliation = "RWTH - PADS",
			author = "Viki Peeva",
			email = "viki.peeva@rwth-aachen.de",
			uiLabel = "Local Process Models Discovery Based on Set of Places given Log"
	)
	@PluginVariant(
			variantLabel = "Local Process Models Discovery Based on Set of Places given Log",
			requiredParameterLabels = {0}
	)
	public static LPMResult mineLPMs(UIPluginContext context, XLog log) {
		Main.setUp(context);

		PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);

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

		return (LPMResult) Main.run(log, parameters)[0];
	}

	@UITopiaVariant(
			affiliation = "RWTH - PADS",
			author = "Viki Peeva",
			email = "viki.peeva@rwth-aachen.de",
			uiLabel = "Local Process Models Discovery Based on Set of Places given Petri Net (faster)"
	)
	@PluginVariant(
			variantLabel = "Local Process Models Discovery Based on Set of Places given Petri Net (faster)",
			requiredParameterLabels = {0, 2}
	)
	public static LPMResult mineLPMs(UIPluginContext context, XLog log, Petrinet petrinet) {
		Main.setUp(context);

		PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);

		// show wizard
		Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryParameters>> stepMap = new HashMap<>();
		stepMap.put(PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY, new LPMDiscoveryWizardStep(log));
//		stepMap.put(PlaceBasedLPMDiscoveryWizard.EVALUATION, new EvaluationWizardStep());
		PlaceBasedLPMDiscoveryWizard wizard = new PlaceBasedLPMDiscoveryWizard(stepMap, false);
		parameters = ProMWizardDisplay.show(context, wizard, parameters);

		if (parameters == null)
			return null;

		return (LPMResult) Main.run(PlaceUtils.getPlacesFromPetriNet(context, petrinet), log, parameters)[0];
	}

	// TODO: What is this doing here???


	@UITopiaVariant(
			affiliation = "RWTH - PADS",
			author = "Viki Peeva",
			email = "viki.peeva@rwth-aachen.de",
			uiLabel = "Local Process Models Discovery Based on Set of Places given Places (faster)"
	)
	@PluginVariant(
			variantLabel = "Local Process Models Discovery Based on Set of Places given Places (faster)",
			requiredParameterLabels = {0, 1}
	)
	public static LPMResult mineLPMs(UIPluginContext context, XLog log, PlaceSet placeSet) {
		Main.setUp(context);

		PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);

		// show wizard
		Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryParameters>> stepMap = new HashMap<>();
		stepMap.put(PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY, new LPMDiscoveryWizardStep(log));
//		stepMap.put(PlaceBasedLPMDiscoveryWizard.EVALUATION, new EvaluationWizardStep());
		PlaceBasedLPMDiscoveryWizard wizard = new PlaceBasedLPMDiscoveryWizard(stepMap, false);
		parameters = ProMWizardDisplay.show(context, wizard, parameters);

		if (parameters == null)
			return null;

		return (LPMResult) Main.run(placeSet.getElements(), log, parameters)[0];
	}

	@PluginVariant(
			variantLabel = "Local Process Models Discovery Based on Set of Places given Log",
			requiredParameterLabels = {0}
	)
	public static Object[] mineLPMs(PluginContext context, XLog log) {
		Main.setUp(context);

		PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);
		return Main.run(log, parameters);
	}

	@PluginVariant(
			variantLabel = "Local Process Models Discovery Based on Set of Places given Places (faster)",
			requiredParameterLabels = {0, 1}
	)
	public static Object[] mineLPMs(PluginContext context, XLog log, PlaceSet placeSet) {
		Main.setUp(context);

		PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);

		return Main.run(placeSet.getElements(), log, parameters);
	}

	@PluginVariant(
			variantLabel = "Local Process Models Discovery Based on Set of Places given Petri Net (faster)",
			requiredParameterLabels = {0, 2}
	)
	public static Object[] mineLPMs(PluginContext context, XLog log, Petrinet petrinet) {
		Main.setUp(context);

		PlaceBasedLPMDiscoveryParameters parameters = new PlaceBasedLPMDiscoveryParameters(log);
		PlaceSet places = new PlaceSet(PlaceUtils.getPlacesFromPetriNet(context, petrinet));

		return run(context, log, places, parameters);
	}

	@PluginVariant(
			variantLabel = "Local Process Models Discovery Based on Set of Places given Log",
			requiredParameterLabels = {0, 3}
	)
	public static Object[] mineLPMs(PluginContext context, XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
		return run(context, log, parameters);
	}

	@PluginVariant(
			variantLabel = "Local Process Models Discovery Based on Set of Places given Places (faster)",
			requiredParameterLabels = {0, 1, 3}
	)
	public static Object[] mineLPMs(PluginContext context, XLog log, PlaceSet placeSet, PlaceBasedLPMDiscoveryParameters parameters) {
		return run(context, log, placeSet, parameters);
	}

	private static Object[] run(PluginContext context, XLog log, PlaceBasedLPMDiscoveryParameters parameters) {
		Main.setUp(context);
		return Main.run(log, parameters);
	}

	private static Object[] run(PluginContext context, XLog log, PlaceSet placeSet, PlaceBasedLPMDiscoveryParameters parameters) {
		Main.setUp(context);
		return Main.run(placeSet.getElements(), log, parameters);
	}


}
