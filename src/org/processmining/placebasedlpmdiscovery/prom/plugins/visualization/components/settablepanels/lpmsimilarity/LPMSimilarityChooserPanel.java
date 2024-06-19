package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponent;
import org.processmining.placebasedlpmdiscovery.view.model.lpmdistances.ModelDistanceVM;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.Objects;

public class LPMSimilarityChooserPanel extends JPanel implements LPMDViewComponent {

    private final Map<String, JPanel> similaritySetupPanelMap;
    private final JPanel distMethodParam;

    @Inject
    public LPMSimilarityChooserPanel(Map<String, JPanel> similaritySetupPanelMap) {
        this.similaritySetupPanelMap = similaritySetupPanelMap;

        this.setLayout(new BorderLayout(10, 0));
        this.setBorder(new TitledBorder("LPM Similarity Chooser"));

        // the distance extraction method chooser
        JPanel distMethodPanel = new JPanel();
        distMethodPanel.setLayout(new BoxLayout(distMethodPanel, BoxLayout.X_AXIS));
        distMethodPanel.add(new JLabel("Distance Extraction Method:"));
        distMethodPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<String> distMethodComboBox = new JComboBox<>(new String[]{"Model Similarity", "Data Attributes", "Mixed"});
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

    private void distanceMethodChanged(String distMethod, JComponent container) {
        if (container.getComponents().length > 0) {
            container.remove(0);
        }
        container.setBorder(new TitledBorder(distMethod));
        container.add(getDistMethodParametersView(distMethod));
        container.revalidate();
    }

    private Component getDistMethodParametersView(String distMethod) {
        switch (distMethod) {
            case "Model Similarity":
                return similaritySetupPanelMap.get(distMethod);
            case "Data Attributes":
                return similaritySetupPanelMap.get(distMethod); //getDistMethodSetupPanelForDataAttributes();
            case "Mixed":
                return similaritySetupPanelMap.get(distMethod);
        }
        throw new IllegalArgumentException("The distance method " + distMethod + " is unknown.");
    }

    @Override
    public Component getComponent() {
        return this;
    }

    @Override
    public ModelDistanceVM getModel() {
        return null;
    }
}
