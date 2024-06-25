package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingController;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponent;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponentType;
import org.processmining.placebasedlpmdiscovery.service.lpms.LPMSetService;
import org.processmining.placebasedlpmdiscovery.view.datacommunication.DataCommunicationControllerVM;

import javax.swing.*;
import java.awt.*;

public class GroupingSetupPanel extends JPanel {

    private final GroupingConfig groupingConfig;

    @Inject
    public GroupingSetupPanel(DataCommunicationControllerVM dc, LPMSetService lpmSetService,
                              GroupingController groupingController,
                              GroupingConfig groupingConfig, ComponentFactory componentFactory) {
        this.groupingConfig = groupingConfig;

        this.setLayout(new BorderLayout());

        JPanel setupPanels = new JPanel();
        setupPanels.setLayout(new GridLayout(1, 2));
        LPMDViewComponent lpmSimilarityPanel =
                componentFactory.create(LPMDViewComponentType.LPMSimilarityChooser);
        setupPanels.add(lpmSimilarityPanel.getComponent());
        ClusteringSetupPanel clusteringPanel = new ClusteringSetupPanel(this.groupingConfig.getClusteringConfig());
        setupPanels.add(clusteringPanel);
        this.add(setupPanels, BorderLayout.CENTER);

        JButton btnCluster = new JButton("Run Clustering");
        btnCluster.addActionListener(e -> {
//            dc.emit(new BlockViewEmittableDataVM("Clustering is running"));
            groupingController.groupLPMs(lpmSetService.getLPMSet().getAllLPMs(), groupingConfig);
//            dc.emit(new RunLPMGroupingEmittableData(groupingConfig.getIdentifier(),
//                    groupingConfig.getClusteringConfig().getClusteringAlgorithm(),
//                    groupingConfig.getClusteringConfig().getClusteringParam(),
//                    groupingConfig.getModelDistanceConfig(), lpmSetService.getLPMSet().getAllLPMs()));
        });
        this.add(btnCluster, BorderLayout.PAGE_END);
    }
}