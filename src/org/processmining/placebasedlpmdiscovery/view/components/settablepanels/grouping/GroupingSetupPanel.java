package org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.grouping.DefaultGroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingController;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingUtils;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponentType;
import org.processmining.placebasedlpmdiscovery.service.lpms.LPMSetService;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponent;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.grouping.ClusteringViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.grouping.GroupingViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.clustering.ClusteringSetupPanel;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class GroupingSetupPanel extends JPanel implements ConfigurationComponent {
    // components
    private JTextField txtClustId;
    private ClusteringSetupPanel clusteringPanel;

    @Inject
    public GroupingSetupPanel(DataCommunicationControllerVM dc, LPMSetService lpmSetService,
                              GroupingController groupingController,
                              ComponentFactory componentFactory) {
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
        LPMDViewComponent lpmSimilarityPanel =
                componentFactory.create(LPMDViewComponentType.LPMSimilarityChooser);
        setupPanels.add(lpmSimilarityPanel.getComponent());
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
    public ViewConfiguration getConfiguration() {
        return new GroupingViewConfiguration(txtClustId.getText(),
                (ClusteringViewConfiguration) clusteringPanel.getConfiguration());
    }
}