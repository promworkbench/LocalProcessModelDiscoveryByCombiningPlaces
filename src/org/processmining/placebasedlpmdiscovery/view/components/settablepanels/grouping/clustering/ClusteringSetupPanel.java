package org.processmining.placebasedlpmdiscovery.view.components.settablepanels.grouping.clustering;

import org.processmining.placebasedlpmdiscovery.grouping.ClusteringAlgorithm;
import org.processmining.placebasedlpmdiscovery.prom.plugins.visualization.components.ComponentFactory;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.ConfigurationComponent;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.SimpleMapViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.ViewConfiguration;
import org.processmining.placebasedlpmdiscovery.view.components.configurationcomponents.configurations.grouping.ClusteringViewConfiguration;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.HashMap;
import java.util.Objects;

public class ClusteringSetupPanel extends JPanel implements ConfigurationComponent {

    // components
    private JComboBox<ClusteringAlgorithm> clustAlgComboBox;
    private JComponent clustAlgParamContainer;
    private ConfigurationComponent clustAlgParamComp;


    public ClusteringSetupPanel() {
        this.setLayout(new BorderLayout(5, 10));
        this.setBorder(new TitledBorder("Clustering Setup"));

        // the clustering algorithm chooser
        setClusteringAlgorithmChooser();

        // the clustering algorithm parameter panel
        setClusteringAlgorithmParameterComponent();
    }

    private void setClusteringAlgorithmParameterComponent() {
        this.clustAlgParamContainer = new JPanel();
        this.clustAlgParamContainer.setPreferredSize(new Dimension(200, 100));
        this.clustAlgParamContainer.setBorder(new TitledBorder("Parameters"));
        this.clustAlgParamComp = getClusteringAlgorithmParametersView(
                (ClusteringAlgorithm) Objects.requireNonNull(clustAlgComboBox.getSelectedItem()));
        this.clustAlgParamContainer.add(this.clustAlgParamComp.getComponent());
        this.add(this.clustAlgParamContainer, BorderLayout.CENTER);
    }

    private void setClusteringAlgorithmChooser() {
        JPanel clustAlgPanel = new JPanel();
        clustAlgPanel.setLayout(new BoxLayout(clustAlgPanel, BoxLayout.X_AXIS));
        clustAlgPanel.add(new JLabel("Clustering Algorithm:"));
        clustAlgPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        this.clustAlgComboBox = new JComboBox<>(ClusteringAlgorithm.values());
        clustAlgComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                clusteringAlgorithmChanged((ClusteringAlgorithm) e.getItem());
            }
        });
        clustAlgPanel.add(clustAlgComboBox);
        this.add(clustAlgPanel, BorderLayout.PAGE_START);
    }

    private void clusteringAlgorithmChanged(ClusteringAlgorithm algorithm) {
        // remove the parameters panel for the previously selected algorithm
        if (this.clustAlgParamContainer.getComponents().length > 0) {
            this.clustAlgParamContainer.remove(0);
        }
        this.clustAlgParamContainer.add(getClusteringAlgorithmParametersView(algorithm).getComponent());
        this.revalidate();
    }

    private ConfigurationComponent getClusteringAlgorithmParametersView(ClusteringAlgorithm algorithm) {
        switch (algorithm) {
            case Hierarchical:
                return getParameterPanelForHierarchicalClust();
//            case KMedoids:
//                return new JLabel("KMedoids");
//            case DBSCAN:
//                return new JLabel("DBSCAN");
//            case Spectral:
//                return new JLabel("Spectral");
//            case MinEntropy:
//                return new JLabel("MinEntropy");
        }
        throw new IllegalArgumentException("The clustering algorithm " + algorithm + " is unknown.");
    }

    private ConfigurationComponent getParameterPanelForHierarchicalClust() {
        return new HierarchicalClusteringSetupPanel();
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public ViewConfiguration getConfiguration() {
        return new ClusteringViewConfiguration(
                (ClusteringAlgorithm) this.clustAlgComboBox.getSelectedItem(),
                ((SimpleMapViewConfiguration) this.clustAlgParamComp.getConfiguration()).getParameterMap());
    }
}
