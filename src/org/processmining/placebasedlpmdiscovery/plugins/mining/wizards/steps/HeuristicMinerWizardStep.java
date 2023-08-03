package org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.steps;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.HeuristicMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.LogUtility;
import org.processmining.plugins.heuristicsnet.miner.heuristics.miner.gui.ParametersPanel;

import javax.swing.*;

public class HeuristicMinerWizardStep implements ProMWizardStep<PlaceBasedLPMDiscoveryParameters> {

    private static final String TITLE = "Heuristic Miner Place Discovery Algorithm";

    private ParametersPanel wrappedStep;
    private HeuristicMinerPlaceDiscoveryParameters parameters;

    public HeuristicMinerWizardStep(XLog log) {
        this.wrappedStep = new ParametersPanel(LogUtility.getEventClassifiers(log));
        this.wrappedStep.removeAndThreshold();
        this.parameters = new HeuristicMinerPlaceDiscoveryParameters();
    }

    @Override
    public PlaceBasedLPMDiscoveryParameters apply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        if (!canApply(placeBasedLPMDiscoveryParameters, jComponent))
            return placeBasedLPMDiscoveryParameters;
        this.parameters.setSettings(this.wrappedStep.getSettings());
        placeBasedLPMDiscoveryParameters.setPlaceDiscoveryParameters(this.parameters);
        return placeBasedLPMDiscoveryParameters;
    }

    @Override
    public boolean canApply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        return jComponent instanceof ParametersPanel;
    }

    @Override
    public JComponent getComponent(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters) {
        return this.wrappedStep;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
