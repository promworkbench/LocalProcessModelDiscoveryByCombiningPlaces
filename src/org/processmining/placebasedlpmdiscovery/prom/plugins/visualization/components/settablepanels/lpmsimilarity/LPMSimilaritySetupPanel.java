package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity;

import com.google.inject.Inject;
import csplugins.id.mapping.ui.CheckComboBox;
import org.processmining.placebasedlpmdiscovery.lpmdistances.ModelDistanceConfig;
import org.processmining.placebasedlpmdiscovery.lpmdistances.processmodelsimilarity.ProcessModelSimilarityMeasure;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.Objects;

public class LPMSimilaritySetupPanel extends JPanel {

    private Map<String, JPanel> similaritySetupPanelMap;
    private ModelDistanceConfig distanceConfig;

    private final JPanel distMethodParam;

    @Inject
    public LPMSimilaritySetupPanel(ModelDistanceConfig distanceConfig,
                                   Map<String, JPanel> similaritySetupPanelMap) {
        this.distanceConfig = distanceConfig;
        this.similaritySetupPanelMap = similaritySetupPanelMap;

        this.setLayout(new BorderLayout(10, 0));
        this.setBorder(new TitledBorder("LPM Similarity Measure Setup"));

        // the distance extraction method chooser
        JPanel distMethodPanel = new JPanel();
        distMethodPanel.setLayout(new BoxLayout(distMethodPanel, BoxLayout.X_AXIS));
        distMethodPanel.add(new JLabel("Distance Extraction Method:"));
        distMethodPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<String> distMethodComboBox = new JComboBox<>(new String[] {"Model Similarity", "Data Attributes", "Mixed"});
        distMethodPanel.add(distMethodComboBox);
        this.add(distMethodPanel, BorderLayout.PAGE_START);

        // the distance method parameter setup panel
        this.distMethodParam = new JPanel();
        this.distMethodParam.setPreferredSize(new Dimension(200, 100));
        this.distMethodParam.setBorder(new TitledBorder("Parameters"));

        distMethodComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                distanceMethodChanged((String) e.getItem(), this.distMethodParam);
            }
        });
        distanceMethodChanged((String) Objects.requireNonNull(distMethodComboBox.getSelectedItem()), this.distMethodParam);
        this.add(this.distMethodParam, BorderLayout.CENTER);
    }

    private Component getDistMethodParametersView(String distMethod) {
        switch (distMethod) {
            case "Model Similarity":
                return getDistMethodSetupPanelForModelSimilarity();
            case "Data Attributes":
                return similaritySetupPanelMap.get(distMethod); //getDistMethodSetupPanelForDataAttributes();
            case "Mixed":
                return getDistMethodSetupPanelForMixed();
        }
        throw new IllegalArgumentException("The distance method " + distMethod + " is unknown.");
    }

    private JPanel getDistMethodSetupPanelForMixed() {
        JPanel mixedPanel = new JPanel();
        mixedPanel.setLayout(new BorderLayout());

        // table where measures are shown
        DefaultTableModel measuresTableModel = new DefaultTableModel(new Object[] { "Measure", "Weight" }, 0);
        JTable measuresTable = new JTable(measuresTableModel);
        JScrollPane js = new JScrollPane(measuresTable,
                ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        js.setPreferredSize(new Dimension(260, 200));
        mixedPanel.add(js, BorderLayout.CENTER);

        // button palette for add, remove, and edit
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        JButton addBtn = new JButton("Add");
        addBtn.addActionListener(e -> {
            getAddDistMeasureInMixedFrame().setVisible(true);
        });
        btnPanel.add(addBtn);

        JButton editBtn = new JButton("Edit");
        btnPanel.add(editBtn);

        JButton removeBtn = new JButton("Remove");
        btnPanel.add(removeBtn);

        mixedPanel.add(btnPanel, BorderLayout.PAGE_END);

        return mixedPanel;
    }

    private JFrame getAddDistMeasureInMixedFrame() {
        JFrame frame = new JFrame();
        JPanel content = new JPanel();

        content.setLayout(new BorderLayout(10, 0));
        content.setBorder(new TitledBorder("Mixed Part Measure Setup"));

        // the distance extraction method chooser
        JPanel distMethodPanel = new JPanel();
        distMethodPanel.setLayout(new BoxLayout(distMethodPanel, BoxLayout.X_AXIS));
        distMethodPanel.add(new JLabel("Distance Extraction Method:"));
        distMethodPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<String> distMethodComboBox = new JComboBox<>(new String[] {"Model Similarity", "Data Attributes", "Mixed"});
        distMethodPanel.add(distMethodComboBox);
        content.add(distMethodPanel, BorderLayout.PAGE_START);

        // the distance method parameter setup panel
        JPanel distMethodParam = new JPanel();
        distMethodParam.setPreferredSize(new Dimension(200, 100));
        distMethodParam.setBorder(new TitledBorder("Parameters"));

        distMethodComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                distanceMethodChanged((String) e.getItem(), this.distMethodParam);
            }
        });
        distanceMethodChanged((String) Objects.requireNonNull(distMethodComboBox.getSelectedItem()), this.distMethodParam);
        content.add(this.distMethodParam, BorderLayout.CENTER);

        frame.add(content);
        return frame;
    }

    private JPanel getDistMethodSetupPanelForMixed1() {
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
        txtFormula.setPreferredSize(new Dimension(400, 100));
        mixedPanel.add(txtFormula, BorderLayout.CENTER);

        return mixedPanel;
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

    private void distanceMethodChanged(String distMethod, JComponent container) {
        if (container.getComponents().length > 0) {
            container.remove(0);
        }
        container.setBorder(new TitledBorder(distMethod));
        container.add(getDistMethodParametersView(distMethod));
        container.revalidate();
    }
}
