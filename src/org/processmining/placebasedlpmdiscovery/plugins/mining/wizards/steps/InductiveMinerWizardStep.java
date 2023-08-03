package org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.steps;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.parameters.InductiveMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.plugins.InductiveMiner.plugins.dialogs.IMMiningDialog;

import javax.swing.*;

public class InductiveMinerWizardStep implements ProMWizardStep<PlaceBasedLPMDiscoveryParameters> {

    private IMMiningDialog wrappedStep;
    private InductiveMinerPlaceDiscoveryParameters parameters;

    public InductiveMinerWizardStep(XLog log) {
        this.wrappedStep = new IMMiningDialog(log);
        this.parameters = new InductiveMinerPlaceDiscoveryParameters();
    }

    @Override
    public PlaceBasedLPMDiscoveryParameters apply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        if (!canApply(placeBasedLPMDiscoveryParameters, jComponent))
            return placeBasedLPMDiscoveryParameters;
        this.parameters.setMiningParameters(this.wrappedStep.getMiningParameters());
        this.parameters.setVariant(this.wrappedStep.getVariant());
        placeBasedLPMDiscoveryParameters.setPlaceDiscoveryParameters(this.parameters);
        return placeBasedLPMDiscoveryParameters;
    }

    @Override
    public boolean canApply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        return jComponent instanceof IMMiningDialog;
    }

    @Override
    public JComponent getComponent(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters) {
        return this.wrappedStep;
    }

    @Override
    public String getTitle() {
        return "Inductive Miner Place Discovery Algorithm";
    }
}
