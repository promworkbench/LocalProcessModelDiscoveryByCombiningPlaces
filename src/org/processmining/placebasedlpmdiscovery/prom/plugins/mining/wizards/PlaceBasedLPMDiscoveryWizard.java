package org.processmining.placebasedlpmdiscovery.prom.plugins.mining.wizards;

import org.processmining.framework.util.ui.wizard.MapWizard;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscoveryAlgorithmId;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryParameters;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class PlaceBasedLPMDiscoveryWizard extends MapWizard<PlaceBasedLPMDiscoveryParameters, String> {

    public static final String INITIAL_KEY = "PlaceDiscoveryAlgorithmChoiceParameters";
    public static final String PD_EST_MINER = "PlaceDiscoveryESTMinerParameters";
    public static final String PD_INDUCTIVE_MINER = "PlaceDiscoveryInductiveMinerParameters";
    public static final String PD_HEURISTIC_MINER = "PlaceDiscoveryHeuristicMinerParameters";
    public static final String LPM_DISCOVERY = "LPMDiscoveryParameters";
    public static final String EVALUATION = "EvaluationParameters";
    public static final String LPM_CONTEXT = "ContextParameters";

    private final boolean discoverPlaces;
    private final boolean withContext;

    public PlaceBasedLPMDiscoveryWizard(Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryParameters>> steps, boolean discoverPlaces, boolean withContext) {
        super(steps);
        this.discoverPlaces = discoverPlaces;
        this.withContext = withContext;
    }

    public PlaceBasedLPMDiscoveryWizard(Map<String, ProMWizardStep<PlaceBasedLPMDiscoveryParameters>> steps, boolean discoverPlaces) {
        this(steps, discoverPlaces, false);
    }

    @Override
    public Collection<String> getFinalKeys(MapModel<PlaceBasedLPMDiscoveryParameters, String> mapModel) {
        if (withContext)
            return Collections.singletonList(PlaceBasedLPMDiscoveryWizard.LPM_CONTEXT);
        return Collections.singletonList(PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY);
    }

    @Override
    public String getInitialKey(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters) {
        if (discoverPlaces)
            return PlaceBasedLPMDiscoveryWizard.INITIAL_KEY;
        return PlaceBasedLPMDiscoveryWizard.LPM_DISCOVERY;
    }

    @Override
    public String getNextKey(MapModel<PlaceBasedLPMDiscoveryParameters, String> mapModel) {
        if (mapModel.getCurrent().equals(INITIAL_KEY)) {
            if (mapModel.getModel().isUseDefaultPlaceDiscoveryParameters()) {
                mapModel.getModel().setDefaultPlaceDiscoveryParameters();
                return LPM_DISCOVERY;
            }
            if (mapModel.getModel().getPlaceDiscoveryAlgorithmId() == PlaceDiscoveryAlgorithmId.ESTMiner)
                return PD_EST_MINER;
            if (mapModel.getModel().getPlaceDiscoveryAlgorithmId() == PlaceDiscoveryAlgorithmId.InductiveMiner)
                return PD_INDUCTIVE_MINER;
            if (mapModel.getModel().getPlaceDiscoveryAlgorithmId() == PlaceDiscoveryAlgorithmId.HeuristicMiner)
                return PD_HEURISTIC_MINER;
        } else if (withContext && mapModel.getCurrent().equals(LPM_DISCOVERY)) {
            return LPM_CONTEXT;
        }
        return LPM_DISCOVERY;
    }
}
