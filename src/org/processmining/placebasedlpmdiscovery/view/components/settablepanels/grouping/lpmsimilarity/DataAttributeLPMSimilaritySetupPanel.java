package org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity;

import com.google.inject.Inject;
import csplugins.id.mapping.ui.CheckComboBox;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.service.eventlog.EventLogService;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;

import javax.swing.*;
import java.awt.*;

public class DataAttributeLPMSimilaritySetupPanel extends JPanel implements LPMSimilaritySetupComponent {

    private final EventLogService eventLogService;

    @Inject
    public DataAttributeLPMSimilaritySetupPanel(EventLogService eventLogService) {
        this.eventLogService = eventLogService;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(new JLabel("Data Attributes:"));
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        CheckComboBox dataAttrComboBox = new CheckComboBox(this.eventLogService.getEventAttributesKeys(), true);
        this.add(dataAttrComboBox);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public ViewConfiguration getConfiguration() {
        return null;
    }
}
