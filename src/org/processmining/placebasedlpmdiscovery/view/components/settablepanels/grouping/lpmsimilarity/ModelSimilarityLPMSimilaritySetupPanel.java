package org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity;

import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityMeasure;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.SimpleMapViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity.LPMSimilarityViewConfiguration;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModelSimilarityLPMSimilaritySetupPanel extends JPanel implements LPMSimilaritySetupComponent {

    // components
    private final JComboBox<ProcessModelSimilarityMeasure> modelSimComboBox;

    public ModelSimilarityLPMSimilaritySetupPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(new JLabel("Model Similarity:"));
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        this.modelSimComboBox = new JComboBox<>(ProcessModelSimilarityMeasure.values());
        this.add(modelSimComboBox);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public ViewConfiguration getConfiguration() {
        return new LPMSimilarityViewConfiguration(
                "Model Similarity",
                Collections.singletonMap(
                        "modelSimilarity",
                        Objects.requireNonNull(this.modelSimComboBox.getSelectedItem())));
    }
}
