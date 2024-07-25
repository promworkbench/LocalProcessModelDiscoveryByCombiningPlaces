package org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingController;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingUtils;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponent;
import org.processmining.placebasedlpmdiscovery.service.lpms.LPMSetService;
import org.processmining.placebasedlpmdiscovery.view.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponent;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.StandardConfigurationComponentType;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.grouping.ClusteringViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.grouping.GroupingViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.lpmsimilarity.LPMSimilarityViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.clustering.ClusteringSetupPanel;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;
import org.processmining.placebasedlpmdiscovery.view.model.lpmdistances.ModelDistanceVM;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GroupingSetupPanel extends JPanel implements LPMDViewComponent, ConfigurationComponent {
    // components
    private JTextField txtClustId;
    private ClusteringSetupPanel clusteringPanel;
    private ConfigurationComponent lpmSimilarityChooserPanel;

    @Inject
    public GroupingSetupPanel(DataCommunicationControllerVM dc,
                              LPMSetService lpmSetService,
                              GroupingController groupingController,
                              ConfigurationComponentFactory componentFactory) {
        this.setLayout(new BorderLayout());

        JPanel clustIdPanel = new JPanel();
        clustIdPanel.setBorder(new TitledBorder("Clustering Id"));
        clustIdPanel.setLayout(new BoxLayout(clustIdPanel, BoxLayout.X_AXIS));
//        clustIdPanel.add(new JLabel("Clustering Id:"));
//        clustIdPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        txtClustId = new JTextField("default");
        clustIdPanel.add(txtClustId);
        this.add(clustIdPanel, BorderLayout.PAGE_START);

        JPanel setupPanels = new JPanel();
        setupPanels.setLayout(new GridLayout(1, 2));
        this.lpmSimilarityChooserPanel = componentFactory
                .create(StandardConfigurationComponentType.LPMSimilarityConfigurationComponent);
        setupPanels.add(lpmSimilarityChooserPanel.getComponent());
        clusteringPanel = new ClusteringSetupPanel();
        setupPanels.add(clusteringPanel);
        this.add(setupPanels, BorderLayout.CENTER);

        JButton btnCluster = new JButton("Run Clustering");
        btnCluster.addActionListener(e -> {
//            dc.emit(new BlockViewEmittableDataVM("Clustering is running"));
            groupingController.groupLPMs(lpmSetService.getLPMSet().getAllLPMs(),
                    GroupingUtils.createGroupingConfig(this.getConfiguration().getMap()));
//            dc.emit(new RunLPMGroupingEmittableData(groupingConfig.getIdentifier(),
//                    groupingConfig.getClusteringConfig().getClusteringAlgorithm(),
//                    groupingConfig.getClusteringConfig().getClusteringParam(),
//                    groupingConfig.getModelDistanceConfig(), lpmSetService.getLPMSet().getAllLPMs()));
        });
        this.add(btnCluster, BorderLayout.PAGE_END);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public ModelDistanceVM getModel() {
        return null;
    }

    @Override
    public ViewConfiguration getConfiguration() {
        return new GroupingViewConfiguration(txtClustId.getText(),
                (ClusteringViewConfiguration) clusteringPanel.getConfiguration(),
                (LPMSimilarityViewConfiguration) lpmSimilarityChooserPanel.getConfiguration());
    }
}