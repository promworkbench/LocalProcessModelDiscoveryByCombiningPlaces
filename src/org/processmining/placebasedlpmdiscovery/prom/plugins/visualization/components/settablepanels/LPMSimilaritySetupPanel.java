package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import csplugins.id.mapping.ui.CheckComboBox;
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

        // the distance method parameter setup panel
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
                return getDistMethodSetupPanelForMixed();
        }
        throw new IllegalArgumentException("The distance method " + distMethod + " is unknown.");
    }

    private JPanel getDistMethodSetupPanelForMixed() {
        JPanel mixedPanel = new JPanel();
//        mixedPanel.setLayout(new BoxLayout(mixedPanel, BoxLayout.Y_AXIS));
        mixedPanel.setLayout(new BorderLayout(0, 5));
        mixedPanel.setBorder(new TitledBorder("Here bin ich"));

        // add additional measure
        JPanel addMeasurePanel = new JPanel();
        addMeasurePanel.setLayout(new BoxLayout(addMeasurePanel, BoxLayout.Y_AXIS));

        JPanel chooseMeasurePanel = new JPanel(); // choose measures
        chooseMeasurePanel.setLayout(new BoxLayout(chooseMeasurePanel, BoxLayout.X_AXIS));
        chooseMeasurePanel.add(new JLabel("Measure"));
        chooseMeasurePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<String> measureComboBox = new JComboBox<>(new String[]{"Model Similarity", "Data Attributes"});
        chooseMeasurePanel.add(measureComboBox);
        addMeasurePanel.add(chooseMeasurePanel);
        addMeasurePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JPanel setWeightPanel = new JPanel(); // set weight
        setWeightPanel.setLayout(new BoxLayout(setWeightPanel, BoxLayout.X_AXIS));
        setWeightPanel.add(new JLabel("Weight"));
        setWeightPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JTextField txtWeight = new JTextField("1");
        txtWeight.setMinimumSize(new Dimension(50, measureComboBox.getSize().height));
        setWeightPanel.add(txtWeight);
        addMeasurePanel.add(setWeightPanel);
        addMeasurePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        JPanel addRemovePanel = new JPanel();
        addRemovePanel.setLayout(new BoxLayout(addRemovePanel, BoxLayout.X_AXIS));
        JButton btnAddMeasure = new JButton("Add"); // add measure button
        addRemovePanel.add(btnAddMeasure);
        JButton btnRemoveMeasure = new JButton("Remove"); // add measure button
        addRemovePanel.add(btnRemoveMeasure);
        addMeasurePanel.add(addRemovePanel);
        mixedPanel.add(addMeasurePanel, BorderLayout.PAGE_START);
//        mixedPanel.add(Box.createRigidArea(new Dimension(0, 10)));

//        // setup of the individual measures
//        JTabbedPane tabPane = new JTabbedPane();
//        JScrollPane scrollPane = new JScrollPane(tabPane);
//        scrollPane.setVisible(false);
//        mixedPanel.add(scrollPane, BorderLayout.CENTER);
//
//        // set action on click
//        btnAddMeasure.addActionListener(e -> {
//            if (!scrollPane.isVisible()) {
//                scrollPane.setVisible(true);
//            }
//            String title = "Var " + tabPane.getTabCount();
//            String measure = (String) Objects.requireNonNull(measureComboBox.getSelectedItem());
//            tabPane.addTab(title, null,
//                    getDistMethodParametersView(measure), measure);
////            setCustomTabFeel(tabPane, title);
//        });

        // show the complete formula
        JTextArea txtFormula = new JTextArea();
//        txtFormula.setPreferredSize(new Dimension(400, 100));
        mixedPanel.add(txtFormula, BorderLayout.CENTER);

        return mixedPanel;
    }

    private static void setCustomTabFeel(JTabbedPane tabPane, String title) {
        int index = tabPane.indexOfTab(title);
        JPanel pnlTab = new JPanel();
        pnlTab.setLayout(new BoxLayout(pnlTab, BoxLayout.X_AXIS));
        pnlTab.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        JButton btnClose = new JButton("x");
        btnClose.setOpaque(false);
        btnClose.setContentAreaFilled(false);
        btnClose.setBorderPainted(false);

//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.gridx = 0;
//        gbc.gridy = 0;
//        gbc.weightx = 1;

        pnlTab.add(lblTitle);

//        gbc.gridx++;
//        gbc.weightx = 0;
        pnlTab.add(btnClose);

        tabPane.setTabComponentAt(index, pnlTab);
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
