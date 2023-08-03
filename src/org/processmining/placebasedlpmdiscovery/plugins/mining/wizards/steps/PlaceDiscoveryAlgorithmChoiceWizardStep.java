package org.processmining.placebasedlpmdiscovery.plugins.mining.wizards.steps;

import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscoveryAlgorithmId;
import org.processmining.placebasedlpmdiscovery.plugins.mining.PlaceBasedLPMDiscoveryParameters;

import javax.swing.*;

public class PlaceDiscoveryAlgorithmChoiceWizardStep extends ProMPropertiesPanel implements ProMWizardStep<PlaceBasedLPMDiscoveryParameters> {

    private static final String TITLE = "Place Discovery Algorithm Choice";

    ProMComboBox<PlaceDiscoveryAlgorithmId> comboBox;
    JCheckBox checkBox;

    public PlaceDiscoveryAlgorithmChoiceWizardStep() {
        super(TITLE);
        this.comboBox = addComboBox("Algorithm Choice", PlaceDiscoveryAlgorithmId.values());
        this.checkBox = addCheckBox("Use Default Parameters");
    }

    @Override
    public PlaceBasedLPMDiscoveryParameters apply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        if (!canApply(placeBasedLPMDiscoveryParameters, jComponent)) {
            return placeBasedLPMDiscoveryParameters;
        }
        placeBasedLPMDiscoveryParameters.setPlaceDiscoveryAlgorithmId((PlaceDiscoveryAlgorithmId) comboBox.getSelectedItem());
        placeBasedLPMDiscoveryParameters.setUseDefaultPlaceDiscoveryParameters(checkBox.isSelected());
        return placeBasedLPMDiscoveryParameters;
    }

    @Override
    public boolean canApply(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        return jComponent instanceof PlaceDiscoveryAlgorithmChoiceWizardStep;
    }

    @Override
    public JComponent getComponent(PlaceBasedLPMDiscoveryParameters placeBasedLPMDiscoveryParameters) {
        this.comboBox.setSelectedItem(placeBasedLPMDiscoveryParameters.getPlaceDiscoveryAlgorithmId());
        this.checkBox.setSelected(placeBasedLPMDiscoveryParameters.isUseDefaultPlaceDiscoveryParameters());
        return this;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
