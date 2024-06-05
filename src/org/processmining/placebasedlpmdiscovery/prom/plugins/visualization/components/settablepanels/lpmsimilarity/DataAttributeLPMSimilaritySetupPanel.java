package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity;

import com.google.inject.Inject;
import csplugins.id.mapping.ui.CheckComboBox;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;

import javax.swing.*;
import java.awt.*;

public class DataAttributeLPMSimilaritySetupPanel extends JPanel {

    private final XLog log;

    @Inject
    public DataAttributeLPMSimilaritySetupPanel(XLog log) {
        this.log = log;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(new JLabel("Data Attributes:"));
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        CheckComboBox dataAttrComboBox = new CheckComboBox(LogUtils.getEventAttributesKeys(log), true);
        this.add(dataAttrComboBox);
    }
}
