package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import org.processmining.placebasedlpmdiscovery.grouping.ClusteringAlgorithm;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ClusteringSetupPanel extends JPanel {

    private final JComponent clustAlgParam;

    public ClusteringSetupPanel() {
        this.setLayout(new BorderLayout());

        // the clustering algorithm chooser
        JPanel clustAlgPanel = new JPanel();
        clustAlgPanel.setLayout(new BoxLayout(clustAlgPanel, BoxLayout.X_AXIS));
        clustAlgPanel.add(new JLabel("Clustering Algorithm:"));
        clustAlgPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<ClusteringAlgorithm> clustAlgComboBox = new JComboBox<>(ClusteringAlgorithm.values());
        clustAlgComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                clusteringAlgorithmChanged((ClusteringAlgorithm) e.getItem());
            }
        });
        clustAlgPanel.add(clustAlgComboBox);
        this.add(clustAlgPanel, BorderLayout.PAGE_START);

        // the clustering algorithm parameter panel
        this.clustAlgParam = new JPanel();
        this.clustAlgParam.setPreferredSize(new Dimension(200, 100));
        this.clustAlgParam.setBorder(new TitledBorder("Parameters"));
        this.add(this.clustAlgParam, BorderLayout.CENTER);
    }

    private void clusteringAlgorithmChanged(ClusteringAlgorithm algorithm) {
        // remove the parameters panel for the previously selected algorithm
        this.clustAlgParam.remove(0);

    }
}
