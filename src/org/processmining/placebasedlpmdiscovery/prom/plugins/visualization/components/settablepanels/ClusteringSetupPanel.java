package org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.settablepanels;

import org.processmining.placebasedlpmdiscovery.grouping.ClusteringAlgorithm;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Objects;

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
        setClusteringAlgorithmParametersView((ClusteringAlgorithm) Objects.requireNonNull(clustAlgComboBox.getSelectedItem()));
        this.add(this.clustAlgParam, BorderLayout.CENTER);
    }

    private void clusteringAlgorithmChanged(ClusteringAlgorithm algorithm) {
        // remove the parameters panel for the previously selected algorithm
        if (this.clustAlgParam.getComponents().length > 0) {
            this.clustAlgParam.remove(0);
        }

        setClusteringAlgorithmParametersView(algorithm);
    }

    private void setClusteringAlgorithmParametersView(ClusteringAlgorithm algorithm) {
        switch (algorithm) {
            case Hierarchical:
                this.clustAlgParam.add(getParameterPanelForHierarchicalClust());
                break;
            case KMedoids:
                this.clustAlgParam.add(new JLabel("KMedoids"));
                break;
            case DBSCAN:
                this.clustAlgParam.add(new JLabel("DBSCAN"));
                break;
            case Spectral:
                this.clustAlgParam.add(new JLabel("Spectral"));
                break;
            case MinEntropy:
                this.clustAlgParam.add(new JLabel("MinEntropy"));
                break;
        }
        this.revalidate();
    }

    private JPanel getParameterPanelForHierarchicalClust() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        // set num clusters or height
        JPanel numClustersPanel = getNumClustersPanelHierarchical();
        panel.add(numClustersPanel);

        // set linkage
        JPanel linkagePanel = new JPanel();
        linkagePanel.setLayout(new BoxLayout(linkagePanel, BoxLayout.X_AXIS));
        linkagePanel.add(new JLabel("Linkage"));
        linkagePanel.add(Box.createRigidArea(new Dimension(5, 0)));
        JComboBox<String> linkageComboBox = new JComboBox<>(new String[]{"single", "complete", "wpgma", "upgma"});
        linkagePanel.add(linkageComboBox);
        panel.add(linkagePanel);

        return panel;
    }

    private static JPanel getNumClustersPanelHierarchical() {
        JPanel numClustersPanel = new JPanel();
        numClustersPanel.setLayout(new BoxLayout(numClustersPanel, BoxLayout.X_AXIS));

        // create buttons
        JRadioButton btnNumClusters = new JRadioButton("Num Clusters");
        JRadioButton btnHeight = new JRadioButton("Height");
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(btnNumClusters);
        btnGroup.add(btnHeight);

        // create text fields
        JTextField txtNumClusters = new JTextField("30");
        txtNumClusters.setPreferredSize(new Dimension(50, txtNumClusters.getSize().height));
        JTextField txtHeight = new JTextField("0.3");
        txtHeight.setPreferredSize(new Dimension(50, txtHeight.getSize().height));

        // selection action
        btnNumClusters.addActionListener(e -> {
            txtHeight.setEnabled(false);
            txtNumClusters.setEnabled(true);
        });
        btnHeight.addActionListener(e -> {
            txtHeight.setEnabled(true);
            txtNumClusters.setEnabled(false);
        });

        // initial setup
        btnNumClusters.setSelected(true);
        txtHeight.setEnabled(false);

        // add everything to the panel
        numClustersPanel.add(btnNumClusters);
        numClustersPanel.add(txtNumClusters);
        numClustersPanel.add(btnHeight);
        numClustersPanel.add(txtHeight);
        return numClustersPanel;
    }
}
