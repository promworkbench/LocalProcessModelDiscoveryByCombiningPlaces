package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import csplugins.id.mapping.ui.CheckComboBox;
import org.processmining.framework.util.ui.widgets.ProMCheckBoxWithComboBox;
import org.processmining.framework.util.ui.widgets.ProMComboCheckBox;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityMeasure;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Objects;

public class LPMSimilaritySetupPanel extends JPanel {

    private final JPanel distMethodParam;

    public LPMSimilaritySetupPanel() {
        this.setLayout(new BorderLayout(10, 0));
        this.setBorder(new TitledBorder("LPM Similarity Measure Setup"));

        // the distance extraction method chooser
        JPanel distMethodPanel = new JPanel();
        distMethodPanel.setLayout(new BoxLayout(distMethodPanel, BoxLayout.X_AXIS));
        distMethodPanel.add(new JLabel("Distance Extraction Method:"));
        distMethodPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<String> distMethodComboBox = new JComboBox<>(new String[] {"Model Similarity", "Data Attributes", "Mixed"});
        distMethodComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                distanceMethodChanged((String) e.getItem());
            }
        });
        distMethodPanel.add(distMethodComboBox);
        this.add(distMethodPanel, BorderLayout.PAGE_START);

        // the clustering algorithm parameter panel
        this.distMethodParam = new JPanel();
        this.distMethodParam.setPreferredSize(new Dimension(200, 100));
        this.distMethodParam.setBorder(new TitledBorder("Parameters"));
        distanceMethodChanged((String) Objects.requireNonNull(distMethodComboBox.getSelectedItem()));
        this.add(this.distMethodParam, BorderLayout.CENTER);
    }

    private Component getDistMethodParametersView(String distMethod) {
        switch (distMethod) {
            case "Model Similarity":
                return getDistMethodSetupPanelForModelSimilarity();
            case "Data Attributes":
                return getDistMethodSetupPanelForDataAttributes();
            case "Mixed":
                return new JLabel("Mixed");
        }
        throw new IllegalArgumentException("The distance method " + distMethod + " is unknown.");
    }

    private JPanel getDistMethodSetupPanelForDataAttributes() {
        JPanel dataAttrPanel = new JPanel();
        dataAttrPanel.setLayout(new BoxLayout(dataAttrPanel, BoxLayout.X_AXIS));
        dataAttrPanel.add(new JLabel("Data Attributes:"));
        dataAttrPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        CheckComboBox dataAttrComboBox = new CheckComboBox(
                new String[]{"Attr1", "Attr2", "Attr3"}, true);
        dataAttrPanel.add(dataAttrComboBox);
        return dataAttrPanel;
    }

    private JPanel getDistMethodSetupPanelForModelSimilarity() {
        JPanel modelSimPanel = new JPanel();
        modelSimPanel.setLayout(new BoxLayout(modelSimPanel, BoxLayout.X_AXIS));
        modelSimPanel.add(new JLabel("Model Similarity:"));
        modelSimPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<ProcessModelSimilarityMeasure> modelSimComboBox = new JComboBox<>(ProcessModelSimilarityMeasure.values());
        modelSimComboBox.addItemListener(e -> {
            // TODO: setup the chosen similarity measure in the model
        });
        modelSimPanel.add(modelSimComboBox);
        return modelSimPanel;
    }

    private void distanceMethodChanged(String distMethod) {
        if (this.distMethodParam.getComponents().length > 0) {
            this.distMethodParam.remove(0);
        }
        this.distMethodParam.setBorder(new TitledBorder(distMethod));
        this.distMethodParam.add(getDistMethodParametersView(distMethod));
        this.revalidate();
    }
}
