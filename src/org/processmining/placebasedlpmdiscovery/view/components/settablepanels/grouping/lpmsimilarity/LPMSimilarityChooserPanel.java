package org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.lpmsimilarity;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponent;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponent;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.model.lpmdistances.ModelDistanceVM;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Map;
import java.util.Objects;

public class LPMSimilarityChooserPanel extends JPanel implements ConfigurationComponent {

//    private final Map<String, LPMSimilaritySetupComponent> similaritySetupPanelMap;
    private final ConfigurationComponentFactory componentFactory;

    // components
    private final JPanel distMethodParamContainer;
    private LPMSimilaritySetupComponent distMethodParamComp;

    @Inject
    public LPMSimilarityChooserPanel(ConfigurationComponentFactory componentFactory) {
        this.componentFactory = componentFactory;

        this.setLayout(new BorderLayout(10, 0));
        this.setBorder(new TitledBorder("LPM Similarity Chooser"));

        // the distance extraction method chooser
        JPanel distMethodPanel = new JPanel();
        distMethodPanel.setLayout(new BoxLayout(distMethodPanel, BoxLayout.X_AXIS));
        distMethodPanel.add(new JLabel("Distance Extraction Method:"));
        distMethodPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<String> distMethodComboBox = new JComboBox<>(
                new String[]{"Model Similarity", "Data Attributes", "Mixed"});
        distMethodComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                distanceMethodChanged((String) e.getItem());
            }
        });
        distMethodPanel.add(distMethodComboBox);
        this.add(distMethodPanel, BorderLayout.PAGE_START);

        // the distance method parameter setup panel
        this.distMethodParamContainer = new JPanel();
        this.distMethodParamContainer.setPreferredSize(new Dimension(200, 100));
        this.distMethodParamContainer.setBorder(new TitledBorder("Parameters"));
        distanceMethodChanged((String) Objects.requireNonNull(distMethodComboBox.getSelectedItem()));
        this.add(this.distMethodParamContainer, BorderLayout.CENTER);
    }

    private void distanceMethodChanged(String distMethod) {
        if (this.distMethodParamContainer.getComponents().length > 0) {
            this.distMethodParamContainer.remove(0);
        }
        this.distMethodParamContainer.setBorder(new TitledBorder(distMethod));
        this.distMethodParamComp = getDistMethodParametersView(distMethod);
        this.distMethodParamContainer.add(this.distMethodParamComp.getComponent());
        this.distMethodParamContainer.revalidate();
    }

    private LPMSimilaritySetupComponent getDistMethodParametersView(String distMethod) {
        return componentFactory.create(LPMSimilarityConfigurationComponentType.getEnum(distMethod));
    }

    @Override
    public ViewConfiguration getConfiguration() {
        return this.distMethodParamComp.getConfiguration();
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
}
