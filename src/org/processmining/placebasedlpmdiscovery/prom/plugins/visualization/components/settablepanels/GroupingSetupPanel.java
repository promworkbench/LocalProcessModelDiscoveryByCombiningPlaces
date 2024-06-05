package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import com.google.inject.Inject;
import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.LPMDViewComponentType;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels.lpmsimilarity.LPMSimilaritySetupPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GroupingSetupPanel extends JPanel implements ActionListener {

    private final GroupingConfig groupingConfig;

    @Inject
    public GroupingSetupPanel(GroupingConfig groupingConfig,
                              ComponentFactory componentFactory) {
        this.groupingConfig = groupingConfig;

        this.setLayout(new BorderLayout());

        JPanel setupPanels = new JPanel();
        setupPanels.setLayout(new GridLayout(1, 2));
        setupPanels.add(componentFactory.create(LPMDViewComponentType.LPMSimilarityChooser).getComponent());
        setupPanels.add(new ClusteringSetupPanel(this.groupingConfig.getClusteringConfig()));
        this.add(setupPanels, BorderLayout.CENTER);

        JButton btnCluster = new JButton("Run Clustering");
        btnCluster.addActionListener(this);
        this.add(btnCluster, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO: Run clustering
    }
}