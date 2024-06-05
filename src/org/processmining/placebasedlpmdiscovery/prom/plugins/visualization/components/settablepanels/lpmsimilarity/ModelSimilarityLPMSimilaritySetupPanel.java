package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity;

import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityMeasure;

import javax.swing.*;
import java.awt.*;

public class ModelSimilarityLPMSimilaritySetupPanel extends JPanel {

    public ModelSimilarityLPMSimilaritySetupPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(new JLabel("Model Similarity:"));
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<ProcessModelSimilarityMeasure> modelSimComboBox = new JComboBox<>(ProcessModelSimilarityMeasure.values());
        modelSimComboBox.addItemListener(e -> {
            // TODO: setup the chosen similarity measure in the model
        });
        this.add(modelSimComboBox);
    }
}
