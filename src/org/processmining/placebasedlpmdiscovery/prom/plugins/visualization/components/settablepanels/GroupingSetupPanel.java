package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import org.processmining.placebasedlpmdiscovery.grouping.GroupingConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GroupingSetupPanel extends JPanel implements ActionListener {

    private GroupingConfig groupingConfig;

    public GroupingSetupPanel() {
        this.setLayout(new BorderLayout());

        JPanel setupPanels = new JPanel();
        setupPanels.setLayout(new GridLayout(1, 2));
        setupPanels.add(new LPMSimilaritySetupPanel(this.groupingConfig.getModelDistanceConfig()));
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