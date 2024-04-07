package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import org.processmining.placebasedlpmdiscovery.grouping.ClusteringAlgorithm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ClusteringSetupPanel extends JPanel {

    private JComponent clustAlgParam;

    public ClusteringSetupPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // the clustering algorithm chooser
        JComboBox<ClusteringAlgorithm> clustAlgComboBox = new JComboBox<>(ClusteringAlgorithm.values());
        clustAlgComboBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    clusteringAlgorithmChanged((ClusteringAlgorithm) e.getItem());
                }
            }
        });
        this.add(clustAlgComboBox);

        // the clustering algorithm parameter panel
        clustAlgParam = new JPanel();
        this.add(clustAlgParam);
    }

    private void clusteringAlgorithmChanged(ClusteringAlgorithm algorithm) {
        // remove the parameters panel for the previously selected algorithm
        this.clustAlgParam.remove(0);

    }
}
