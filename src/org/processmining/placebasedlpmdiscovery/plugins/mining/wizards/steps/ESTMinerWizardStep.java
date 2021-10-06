package org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.steps;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.placebasedlpmdiscovery.placediscovery.parameters.EstMinerPlaceDiscoveryParameters;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;
import org.processmining.v7.postproc_after_tc.MyMinerWizardStep;
//import org.processmining.v8.eSTMinerGIT.UI;

import javax.swing.*;

public class ESTMinerWizardStep implements ProMWizardStep<PlaceBasedLPMDiscoveryParameters> {

    private MyMinerWizardStep wrappedStep;
    private EstMinerPlaceDiscoveryParameters parameters;

    public ESTMinerWizardStep(XLog log) {
        this.wrappedStep = new MyMinerWizardStep(log);
        this.parameters = new EstMinerPlaceDiscoveryParameters();
//        this.parameters.getWrappedParameters().setRemoveImps(false);
    }

    @Override
    public PlaceBasedLPMDiscoveryParameters apply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        if (!canApply(placeBasedLPMDiscoveryParameters, jComponent))
            return placeBasedLPMDiscoveryParameters;
        this.parameters.setWrappedParameters(this.wrappedStep.apply(this.parameters.getWrappedParameters(), jComponent));
        placeBasedLPMDiscoveryParameters.setPlaceDiscoveryParameters(this.parameters);
        return placeBasedLPMDiscoveryParameters;
    }

    @Override
    public boolean canApply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        return this.wrappedStep.canApply(this.parameters.getWrappedParameters(), jComponent);
    }

    @Override
    public JComponent getComponent(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters) {
        return this.wrappedStep.getComponent(this.parameters.getWrappedParameters());
    }

    @Override
    public String getTitle() {
        return "EfficientST Miner Place Discovery Algorithm";
    }
}
