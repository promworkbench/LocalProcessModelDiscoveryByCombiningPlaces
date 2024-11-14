package org.processmining.placebasedlpmdiscovery.prom.plugins.mining.wizards.steps;

import org.processmining.framework.util.ui.widgets.ProMComboBox;
import org.processmining.framework.util.ui.widgets.ProMPropertiesPanel;
import org.processmining.framework.util.ui.wizard.ProMWizardStep;
import org.processmining.placebasedlpmdiscovery.prom.placediscovery.PlaceDiscoveryAlgorithmId;
import org.processmining.placebasedlpmdiscovery.prom.plugins.mining.PlaceBasedLPMDiscoveryPluginParameters;

import javax.swing.*;

public class PlaceDiscoveryAlgorithmChoiceWizardStep extends ProMPropertiesPanel implements ProMWizardStep<PlaceBasedLPMDiscoveryPluginParameters> {

    private static final String TITLE = "Place Discovery Algorithm Choice";

    ProMComboBox<PlaceDiscoveryAlgorithmId> comboBox;
    JCheckBox checkBox;

    public PlaceDiscoveryAlgorithmChoiceWizardStep() {
        super(TITLE);
        this.comboBox = addComboBox("Algorithm Choice", PlaceDiscoveryAlgorithmId.values());
        this.checkBox = addCheckBox("Use Default Parameters");
    }

    @Override
    public PlaceBasedLPMDiscoveryPluginParameters apply(PlaceBasedLPMDiscoveryPluginParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        if (!canApply(placeBasedLPMDiscoveryParameters, jComponent)) {
            return placeBasedLPMDiscoveryParameters;
        }
        placeBasedLPMDiscoveryParameters.setPlaceDiscoveryAlgorithmId((PlaceDiscoveryAlgorithmId) comboBox.getSelectedItem());
        placeBasedLPMDiscoveryParameters.setUseDefaultPlaceDiscoveryParameters(checkBox.isSelected());
        return placeBasedLPMDiscoveryParameters;
    }

    @Override
    public boolean canApply(PlaceBasedLPMDiscoveryPluginParameters placeBasedLPMDiscoveryParameters, JComponent jComponent) {
        return jComponent instanceof PlaceDiscoveryAlgorithmChoiceWizardStep;
    }

    @Override
    public JComponent getComponent(PlaceBasedLPMDiscoveryPluginParameters placeBasedLPMDiscoveryParameters) {
        this.comboBox.setSelectedItem(placeBasedLPMDiscoveryParameters.getPlaceDiscoveryAlgorithmId());
        this.checkBox.setSelected(placeBasedLPMDiscoveryParameters.isUseDefaultPlaceDiscoveryParameters());
        return this;
    }

    @Override
    public String getTitle() {
        return TITLE;
    }
}
