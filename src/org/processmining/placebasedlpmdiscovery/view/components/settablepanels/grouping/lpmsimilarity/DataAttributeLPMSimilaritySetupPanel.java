package org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity;

import com.google.inject.Inject;
import csplugins.id.mapping.ui.CheckComboBox;
import org.deckfour.xes.model.XLog;
import org.processmining.placebasedlpmdiscovery.service.eventlog.EventLogService;
import org.processmining.placebasedlpmdiscovery.utils.LogUtils;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.SimpleMapViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity.LPMSimilarityViewConfiguration;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.Objects;

public class DataAttributeLPMSimilaritySetupPanel extends JPanel implements LPMSimilaritySetupComponent {

    // services
    private final EventLogService eventLogService;

    // components
    private final CheckComboBox dataAttrComboBox;

    @Inject
    public DataAttributeLPMSimilaritySetupPanel(EventLogService eventLogService) {
        this.eventLogService = eventLogService;

        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
//        this.setPreferredSize(new Dimension(160, this.getPreferredSize().height));
        JLabel lbl = new JLabel("Data Attributes:");
        this.add(lbl);
        this.add(Box.createRigidArea(new Dimension(5, 0)));
        dataAttrComboBox = new CheckComboBox(this.eventLogService.getEventAttributesKeys(), true);
        dataAttrComboBox.setPreferredSize(new Dimension(160, dataAttrComboBox.getPreferredSize().height));
        this.add(dataAttrComboBox);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public ViewConfiguration getConfiguration() {
        return new LPMSimilarityViewConfiguration(
                "Data Attribute",
                Collections.singletonMap("selectedAttributes",
                        Objects.requireNonNull(this.dataAttrComboBox.getSelectedItems())));
    }
}
